package com.brianantonelli.babymon;

import android.content.Context;
import android.graphics.Color;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian Antonelli on 8/11/13.
 */
public class MusicFragment extends ListFragment implements LoaderCallbacks<String[]> {
    private final String TAG = "MusicFragment";
    private static String serviceBaseUrl;
    public List<String> idList;
    private View headerView;
    private int volume = 52;
    private String lastPlayedId;
    private int lastPlayedPosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addHeaderView(headerView);

        idList = new ArrayList<String>();

        MainActivity activity = (MainActivity)getActivity();
        serviceBaseUrl = "http://" + activity.getServerAddress() + ":9080/index.php/";

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        headerView = inflater.inflate(R.layout.music_header, null);
        headerView.setMinimumHeight(50);

        Button stopButton = (Button) headerView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = serviceBaseUrl + "stopsound";
                Log.d(TAG, "Stop: " + url);
                new NetworkTask().execute(url);
            }
        });

        SeekBar volumeBar = (SeekBar) headerView.findViewById(R.id.volumeSlider);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                volume = i;
                if(lastPlayedId != null) playSound(lastPlayedId, Integer.toString(volume));
                // TODO: would be nice to do this at the end of a drag. that way we don't beat on the server
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        volumeBar.setProgress(volume);

        // TODO: use playtime and create a seeker and then when volume is adjusted we can "kinda" resume? does mpg321 support it?

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        l.getChildAt(lastPlayedPosition).setBackgroundColor(Color.WHITE);
        lastPlayedPosition = position;
        v.setBackgroundColor(Color.argb(90, 77, 182, 232));

        String songId =  ((TextView) v.findViewById(R.id.server_id)).getText().toString();
        lastPlayedId = songId;

        playSound(songId, Integer.toString(volume));
    }

    @Override
    public JSONLoader onCreateLoader(int i, Bundle bundle) {
        return new JSONLoader(getActivity(), this);
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {

                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                ((TextView) v.findViewById(R.id.server_id)).setText(idList.get(position));
                ((TextView) v.findViewById(R.id.text)).setText(getItem(position).replace("&amp;", "&"));
                return v;
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    private void playSound(String id, String volume){
        String url = serviceBaseUrl + "playsound/" + id + "/" + volume;
        Log.d(TAG, "Play: " + url);
        new NetworkTask().execute(url);
    }

    static public class JSONLoader extends AsyncTaskLoader<String[]>{
        private MusicFragment fragment;

        public JSONLoader(Context context, MusicFragment fragment){
            super(context);
            this.fragment = fragment;
        }

        @Override
        public String[] loadInBackground() {
            String url = serviceBaseUrl + "sounds";
            String response = sendRequest(url);

            return processResponse(response);
        }

        private String sendRequest(String url){
            BufferedReader input = null; // get the json
            HttpURLConnection httpCon = null; // the http connection object
            StringBuilder response = new StringBuilder(); // hold all the data from the jason in string separated with "\n"

            try {
                URL apiUrl = new URL(url);
                httpCon = (HttpURLConnection) apiUrl.openConnection();

                if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) { // check for connectivity with server
                    return null;
                }
                input = new BufferedReader(new InputStreamReader(httpCon.getInputStream())); // pull all the json from the site
                String line;
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }
            return response.toString();
        }

        private String[] processResponse(String response) {
            try {
                JSONArray songsJson = new JSONArray(response);
                String[] songs = new String[songsJson.length()];
                fragment.idList = new ArrayList<String>(songsJson.length());

                for(int i=0, l=songsJson.length(); i<l; i++){
                    JSONObject song = songsJson.getJSONObject(i);
                    songs[i] = song.getString("title");
                    fragment.idList.add(song.getString("id"));
                }

                return songs;
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class NetworkTask extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... params) {
            String link = params[0];
            HttpGet request = new HttpGet(link);
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            try {
                return client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                client.close();
            }
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            //Do something with result
//            if (result != null)
//                result.getEntity().writeTo(new FileOutputStream(f));
        }
    }

}