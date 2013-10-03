package com.brianantonelli.babymon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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
        String endpoint = ((MainActivity) getActivity()).getServerAddress();

        wv.loadUrl(LOCAL_HTML_FILE + endpoint);

        Button saveButton = (Button) v.findViewById(R.id.saveWebButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureDrawable pd = new PictureDrawable(wv.capturePicture());
                Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawPicture(pd.getPicture());

                // TODO: test on device
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", "description");
            }
        });

        return v;
    }
}
