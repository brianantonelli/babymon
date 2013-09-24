package com.brianantonelli.babymon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.brianantonelli.babymon.mjpegview.IMJPEGViewListener;
import com.brianantonelli.babymon.mjpegview.MJPEGView;

/**
 * Created by monkeymojo on 9/23/13.
 */
public class CameraActivity extends Activity {
    private MJPEGView mv;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        mv = new MJPEGView(this, new IMJPEGViewListener() {
            @Override
            public void success() {

            }

            @Override
            public void error() {

            }

            @Override
            public void hasBitmap(Bitmap bitmap) {

            }
        });
        mv.setSource("http://10.0.1.31:8080/?action=stream");
    }

}
