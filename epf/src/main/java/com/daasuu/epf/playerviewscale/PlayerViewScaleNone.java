package com.daasuu.epf.playerviewscale;

import android.graphics.Point;

public class PlayerViewScaleNone implements PlayerViewScaleType {

    @Override
    public Point requestViewSize(int measuredWidth, int measuredHeight, double adjustedVideoAspect) {
        return new Point(measuredWidth, measuredHeight);
    }
}
