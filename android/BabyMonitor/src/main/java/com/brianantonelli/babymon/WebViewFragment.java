package com.brianantonelli.babymon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by monkeymojo on 9/14/13.
 */
public class WebViewFragment extends Fragment {
    private static final String LOCAL_HTML_FILE = "file:///android_asset/stream_android.html?endpoint=";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_layout, container, false);

        final WebView wv = (WebView) v.findViewById(R.id.webPage);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);

        wv.setBackgroundColor(Color.BLACK);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                MainActivity activity = (MainActivity)getActivity();
                handler.proceed(activity.getServerUsername(), activity.getServerPassword());
            }
        });

        // get server endpoint
        final String endpoint = ((MainActivity) getActivity()).getServerAddress();

        wv.loadUrl(LOCAL_HTML_FILE + endpoint);

        Button saveButton = (Button) v.findViewById(R.id.saveWebButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NetworkTask().doInBackground("http://" + endpoint + "/?action=snapshot");
            }
        });

        // keep the screen on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        return v;
    }

    private class NetworkTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection httpCon;
            try{
                URL apiUrl = new URL(params[0]);
                httpCon = (HttpURLConnection) apiUrl.openConnection();

                InputStream input = httpCon.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                SimpleDateFormat sdf = new SimpleDateFormat();
                String data1 = String.valueOf(String.format("%s/DCIM/babymon/baby-%s.jpg",
                        Environment.getExternalStorageDirectory().getAbsolutePath(),
                        sdf.format(new Date())));

                FileOutputStream stream = new FileOutputStream(data1);

                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
                byte[] byteArray = outstream.toByteArray();

                stream.write(byteArray);
                stream.close();

                return httpCon.getResponseCode();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return HttpURLConnection.HTTP_BAD_REQUEST;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
        }
    }
}

