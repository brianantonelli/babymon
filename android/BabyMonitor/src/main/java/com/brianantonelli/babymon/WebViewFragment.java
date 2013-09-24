package com.brianantonelli.babymon;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.VideoView;

/**
 * Created by monkeymojo on 9/14/13.
 */
public class WebViewFragment extends Fragment {
    private final String url = "http://10.0.1.31:9080/stream_android.html"; // TODO: make configurable
    private final String videoURL = "http://10.0.1.31:9080/live/index.m3u8";
//    "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_layout, container, false);

        WebView wv = (WebView) v.findViewById(R.id.webPage);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);

        wv.setBackgroundColor(Color.BLACK);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
        wv.loadUrl(url);

        return v;

    }
}