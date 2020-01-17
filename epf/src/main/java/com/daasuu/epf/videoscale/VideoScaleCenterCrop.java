package com.daasuu.epf.videoscale;

import android.graphics.Point;
import android.util.Log;
import android.view.View;

/**
 * This will make sure the smallest side fits the parent container, cropping the other
 */
public class VideoScaleCenterCrop implements VideoScaleType {
    private static final String TAG = "VideoScaleCenterCrop";

    @Override
    public void performScale(View view, Point videoSize) {
        if (view.getHeight() == 0 || view.getWidth() == 0) {
            Log.d(TAG, "Cannot apply VideoScaleCenterCrop to a view of size (" + view.getWidth() + ", " + view.getHeight() + ")");
            return;
        }
        if (videoSize.x == 0 || videoSize.y == 0) {
            Log.d(TAG, "Cannot apply VideoScaleCenterCrop to intrinsicVideoSize (" + videoSize.x + ", " + videoSize.y + ")");
            return;
        }

        float xScale = (float) view.getWidth() / videoSize.x;
        float yScale = (float) view.getHeight() / videoSize.y;

        float scale = Math.max(xScale, yScale);
        xScale = scale / xScale;
        yScale = scale / yScale;

        view.setScaleX(xScale);
        view.setScaleY(yScale);
    }
}