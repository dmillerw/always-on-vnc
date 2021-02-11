package com.antlersoft.android.bc;

import android.view.MotionEvent;

/* renamed from: com.antlersoft.android.bc.IBCScaleGestureDetector */
public interface IBCScaleGestureDetector {
    float getCurrentSpan();

    long getEventTime();

    float getFocusX();

    float getFocusY();

    float getPreviousSpan();

    float getScaleFactor();

    long getTimeDelta();

    boolean isInProgress();

    boolean onTouchEvent(MotionEvent motionEvent);
}
