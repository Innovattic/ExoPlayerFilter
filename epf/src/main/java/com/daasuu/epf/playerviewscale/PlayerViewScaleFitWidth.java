package com.daasuu.epf.playerviewscale;

import android.graphics.Point;

public class PlayerViewScaleFitWidth implements PlayerViewScaleType {

    @Override
    public Point requestViewSize(int measuredWidth, int measuredHeight, double adjustedVideoAspect) {
        int viewHeight = (int) (measuredWidth / adjustedVideoAspect);
        return new Point(measuredWidth, viewHeight);
    }
}