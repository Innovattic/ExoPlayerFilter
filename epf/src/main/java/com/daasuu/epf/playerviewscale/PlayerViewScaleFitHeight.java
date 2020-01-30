package com.daasuu.epf.playerviewscale;

import android.graphics.Point;

public class PlayerViewScaleFitHeight implements PlayerViewScaleType {

    @Override
    public Point requestViewSize(int measuredWidth, int measuredHeight, double adjustedVideoAspect) {
        int viewWidth = (int) (measuredHeight * adjustedVideoAspect);
        return new Point(viewWidth, measuredHeight);
    }
}