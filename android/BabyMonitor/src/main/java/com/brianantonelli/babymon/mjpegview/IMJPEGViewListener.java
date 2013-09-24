package com.brianantonelli.babymon.mjpegview;

import android.graphics.Bitmap;

/**
 * Created by monkeymojo on 9/23/13.
 */
public interface IMJPEGViewListener {
    void success();
    void error();
    void hasBitmap(Bitmap bitmap);
}
