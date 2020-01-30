package com.daasuu.epf.playerviewscale;

import android.graphics.Point;

public interface PlayerViewScaleType {
    Point requestViewSize(int measuredWidth, int measuredHeight, double adjustedVideoAspect);
}