package com.daasuu.epf.videoscale;

import android.graphics.Point;
import android.view.View;

public class VideoScaleNone implements VideoScaleType {
    @Override
    public void performScale(View view, Point videoSize) {
        view.setScaleX(1);
        view.setScaleY(1);
    }
}