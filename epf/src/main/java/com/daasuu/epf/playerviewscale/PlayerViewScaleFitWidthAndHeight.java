package com.daasuu.epf.playerviewscale;

import android.graphics.Point;

public class PlayerViewScaleFitWidthAndHeight implements PlayerViewScaleType {

    @Override
    public Point requestViewSize(int measuredWidth, int measuredHeight, double adjustedVideoAspect) {
        int newWidth = (int) (measuredHeight * adjustedVideoAspect);
        if (newWidth <= measuredWidth) {
            return new Point(newWidth, measuredHeight);
        }
        int newHeight = (int) (measuredWidth / adjustedVideoAspect);
        return new Point(measuredWidth, newHeight);
    }
}