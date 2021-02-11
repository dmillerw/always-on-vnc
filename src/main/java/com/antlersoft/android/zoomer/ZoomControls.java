package com.antlersoft.android.zoomer;

import android.androidVNC.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ZoomButton;

public class ZoomControls extends LinearLayout {
    private final ZoomButton mZoomIn;
    private final ImageButton mZoomKeyboard;
    private final ZoomButton mZoomOut;

    public ZoomControls(Context context) {
        this(context, null);
    }

    public ZoomControls(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_controls, (ViewGroup) this, true);
        this.mZoomIn = (ZoomButton) findViewById(R.id.zoomIn);
        this.mZoomOut = (ZoomButton) findViewById(R.id.zoomOut);
        this.mZoomKeyboard = (ImageButton) findViewById(R.id.zoomKeys);
    }

    public void setOnZoomInClickListener(View.OnClickListener listener) {
        this.mZoomIn.setOnClickListener(listener);
    }

    public void setOnZoomOutClickListener(View.OnClickListener listener) {
        this.mZoomOut.setOnClickListener(listener);
    }

    public void setOnZoomKeyboardClickListener(View.OnClickListener listener) {
        this.mZoomKeyboard.setOnClickListener(listener);
    }

    public void setZoomSpeed(long speed) {
        this.mZoomIn.setZoomSpeed(speed);
        this.mZoomOut.setZoomSpeed(speed);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void show() {
        fade(0, 0.0f, 1.0f);
    }

    public void hide() {
        fade(8, 1.0f, 0.0f);
    }

    private void fade(int visibility, float startAlpha, float endAlpha) {
        AlphaAnimation anim = new AlphaAnimation(startAlpha, endAlpha);
        anim.setDuration(500);
        startAnimation(anim);
        setVisibility(visibility);
    }

    public void setIsZoomInEnabled(boolean isEnabled) {
        this.mZoomIn.setEnabled(isEnabled);
    }

    public void setIsZoomOutEnabled(boolean isEnabled) {
        this.mZoomOut.setEnabled(isEnabled);
    }

    public boolean hasFocus() {
        return this.mZoomIn.hasFocus() || this.mZoomOut.hasFocus();
    }
}
