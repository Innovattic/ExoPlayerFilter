package com.daasuu.epf;

import static com.daasuu.epf.chooser.EConfigChooser.EGL_CONTEXT_CLIENT_VERSION;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.daasuu.epf.chooser.EConfigChooser;
import com.daasuu.epf.contextfactory.EContextFactory;
import com.daasuu.epf.filter.GlFilter;
import com.daasuu.epf.playerviewscale.PlayerViewScaleNone;
import com.daasuu.epf.playerviewscale.PlayerViewScaleType;
import com.daasuu.epf.videoscale.VideoScaleNone;
import com.daasuu.epf.videoscale.VideoScaleType;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.video.VideoSize;

/**
 * Created by sudamasayuki on 2017/05/16.
 */
public class EPlayerView extends GLSurfaceView implements Player.Listener {

    private final static String TAG = EPlayerView.class.getSimpleName();

    private final EPlayerRenderer renderer;
    private ExoPlayer player;

    /* Video Aspect according to the video */
    private float measuredVideoAspect = 1f;
    /* Video Aspect according to the video, adjusted to the needs of the filter */
    private float adjustedVideoAspect = measuredVideoAspect;

    /* Video size according to the video */
    private Point intrinsicVideoSize = new Point(0, 0);
    /* Video size according to the video, adjusted to the needs of the filter */
    private Point adjustedVideoSize = new Point(0, 0);

    private GlFilter glFilter = null;

    private PlayerViewScaleType playerViewScaleType = new PlayerViewScaleNone();
    private VideoScaleType videoScaleType = new VideoScaleNone();

    public EPlayerView(Context context) {
        this(context, null);
    }

    public EPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextFactory(new EContextFactory());

        setEGLConfigChooser(new EConfigChooser(8, 8, 8, 8, 16, 0, EGL_CONTEXT_CLIENT_VERSION));

        getHolder().setFormat(PixelFormat.RGBA_8888);

        renderer = new EPlayerRenderer(this);
        setRenderer(renderer);
    }

    public EPlayerView setExoPlayer(ExoPlayer player) {
        if (this.player != null) {
            this.player.release();
            this.player = null;
        }
        this.player = player;
        this.player.addListener(this);
        this.renderer.setExoPlayer(player);
        return this;
    }

    public void setGlFilter(GlFilter glFilter) {
        this.glFilter = glFilter;
        renderer.setGlFilter(glFilter);

        adjustedVideoAspect = calculateAdjustedVideoAspect();

        requestLayout();
    }

    public void setPlayerViewScaleType(PlayerViewScaleType playerViewScaleType) {
        this.playerViewScaleType = playerViewScaleType;
        requestLayout();
    }

    /** Only works if a GlFilter is set, otherwise videoScaleType is ignored */
    public void setVideoScaleType(VideoScaleType videoScaleType) {
        this.videoScaleType = videoScaleType;
        applyVideoScaleType();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Point requestedSize = playerViewScaleType.requestViewSize(getMeasuredWidth(), getMeasuredHeight(), adjustedVideoAspect);
        setMeasuredDimension(requestedSize.x, requestedSize.y);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        applyVideoScaleType();
    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.release();
    }

    //////////////////////////////////////////////////////////////////////////
    // SimpleExoPlayer.VideoListener


    @Override
    public void onVideoSizeChanged(VideoSize videoSize) {

        // Log.d(TAG, "width = " + width + " height = " + height + " unappliedRotationDegrees = " + unappliedRotationDegrees + " pixelWidthHeightRatio = " + pixelWidthHeightRatio);
        updateIntrinsicVideoSize(videoSize.width, videoSize.height);

        measuredVideoAspect = ((float) videoSize.width / videoSize.height) * videoSize.pixelWidthHeightRatio;
        adjustedVideoAspect = calculateAdjustedVideoAspect();
        // Log.d(TAG, "measuredVideoAspect = " + measuredVideoAspect);

        applyVideoScaleType();
        requestLayout();
    }

    @Override
    public void onRenderedFirstFrame() {
        // do nothing
    }

    private float calculateAdjustedVideoAspect() {
        if (glFilter == null) {
            return measuredVideoAspect;
        } else {
            return glFilter.getVideoAspect(measuredVideoAspect);
        }
    }

    private void updateIntrinsicVideoSize(int width, int height) {
        intrinsicVideoSize.x = width;
        intrinsicVideoSize.y = height;
        if (glFilter != null) {
            glFilter.adjustVideoSize(intrinsicVideoSize, adjustedVideoSize);
        }
    }

    private void applyVideoScaleType() {
        videoScaleType.performScale(this, adjustedVideoSize);
    }
}
