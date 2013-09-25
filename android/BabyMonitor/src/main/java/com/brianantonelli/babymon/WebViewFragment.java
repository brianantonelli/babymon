package com.brianantonelli.babymon;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
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

        // get server endpoint
        String endpoint = ((MainActivity) getActivity()).getServerAddress();

        wv.loadUrl("file:///android_asset/stream_android.html?endpoint=" + endpoint);

        return v;

    }
}
