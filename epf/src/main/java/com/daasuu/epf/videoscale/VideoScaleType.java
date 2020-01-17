package com.daasuu.epf.videoscale;

import android.graphics.Point;
import android.view.View;

public interface VideoScaleType {
    void performScale(View view, Point videoSize);
}