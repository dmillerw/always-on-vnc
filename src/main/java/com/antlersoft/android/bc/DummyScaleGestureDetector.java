package com.antlersoft.android.bc;

import android.view.MotionEvent;

/* access modifiers changed from: package-private */
/* renamed from: com.antlersoft.android.bc.DummyScaleGestureDetector */
public class DummyScaleGestureDetector implements IBCScaleGestureDetector {
    DummyScaleGestureDetector() {
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getCurrentSpan() {
        return 0.0f;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public long getEventTime() {
        return 0;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getFocusX() {
        return 0.0f;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getFocusY() {
        return 0.0f;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getPreviousSpan() {
        return 0.0f;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getScaleFactor() {
        return 0.0f;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public long getTimeDelta() {
        return 0;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public boolean isInProgress() {
        return false;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
