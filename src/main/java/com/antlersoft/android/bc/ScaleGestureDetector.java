package com.antlersoft.android.bc;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* renamed from: com.antlersoft.android.bc.ScaleGestureDetector */
class ScaleGestureDetector implements IBCScaleGestureDetector {
    private static final float PRESSURE_THRESHOLD = 0.67f;
    private float mBottomSlopEdge;
    private final Context mContext;
    private MotionEvent mCurrEvent;
    private float mCurrFingerDiffX;
    private float mCurrFingerDiffY;
    private float mCurrLen;
    private float mCurrPressure;
    private final float mEdgeSlop;
    private float mFocusX;
    private float mFocusY;
    private boolean mGestureInProgress;
    private final OnScaleGestureListener mListener;
    private MotionEvent mPrevEvent;
    private float mPrevFingerDiffX;
    private float mPrevFingerDiffY;
    private float mPrevLen;
    private float mPrevPressure;
    private float mRightSlopEdge;
    private float mScaleFactor;
    private boolean mSloppyGesture;
    private long mTimeDelta;

    public ScaleGestureDetector(Context context, OnScaleGestureListener listener) {
        ViewConfiguration config = ViewConfiguration.get(context);
        this.mContext = context;
        this.mListener = listener;
        this.mEdgeSlop = (float) config.getScaledEdgeSlop();
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (this.mGestureInProgress) {
            switch (action & 255) {
                case 2:
                    setContext(event);
                    if (this.mCurrPressure / this.mPrevPressure > PRESSURE_THRESHOLD && this.mListener.onScale(this)) {
                        this.mPrevEvent.recycle();
                        this.mPrevEvent = MotionEvent.obtain(event);
                        break;
                    }
                case 3:
                    if (!this.mSloppyGesture) {
                        this.mListener.onScaleEnd(this);
                    }
                    reset();
                    break;
                case 6:
                    setContext(event);
                    int id = ((65280 & action) >> 8) == 0 ? 1 : 0;
                    this.mFocusX = event.getX(id);
                    this.mFocusY = event.getY(id);
                    if (!this.mSloppyGesture) {
                        this.mListener.onScaleEnd(this);
                    }
                    reset();
                    break;
            }
        } else {
            switch (action & 255) {
                case 2:
                    if (this.mSloppyGesture) {
                        float edgeSlop = this.mEdgeSlop;
                        float rightSlop = this.mRightSlopEdge;
                        float bottomSlop = this.mBottomSlopEdge;
                        float x0 = event.getRawX();
                        float y0 = event.getRawY();
                        float x1 = getRawX(event, 1);
                        float y1 = getRawY(event, 1);
                        boolean p0sloppy = x0 < edgeSlop || y0 < edgeSlop || x0 > rightSlop || y0 > bottomSlop;
                        boolean p1sloppy = x1 < edgeSlop || y1 < edgeSlop || x1 > rightSlop || y1 > bottomSlop;
                        if (!p0sloppy || !p1sloppy) {
                            if (!p0sloppy) {
                                if (!p1sloppy) {
                                    this.mSloppyGesture = false;
                                    this.mGestureInProgress = this.mListener.onScaleBegin(this);
                                    break;
                                } else {
                                    this.mFocusX = event.getX(0);
                                    this.mFocusY = event.getY(0);
                                    break;
                                }
                            } else {
                                this.mFocusX = event.getX(1);
                                this.mFocusY = event.getY(1);
                                break;
                            }
                        } else {
                            this.mFocusX = -1.0f;
                            this.mFocusY = -1.0f;
                            break;
                        }
                    }
                    break;
                case 5:
                    DisplayMetrics metrics = this.mContext.getResources().getDisplayMetrics();
                    this.mRightSlopEdge = ((float) metrics.widthPixels) - this.mEdgeSlop;
                    this.mBottomSlopEdge = ((float) metrics.heightPixels) - this.mEdgeSlop;
                    reset();
                    this.mPrevEvent = MotionEvent.obtain(event);
                    this.mTimeDelta = 0;
                    setContext(event);
                    float edgeSlop2 = this.mEdgeSlop;
                    float rightSlop2 = this.mRightSlopEdge;
                    float bottomSlop2 = this.mBottomSlopEdge;
                    float x02 = event.getRawX();
                    float y02 = event.getRawY();
                    float x12 = getRawX(event, 1);
                    float y12 = getRawY(event, 1);
                    boolean p0sloppy2 = x02 < edgeSlop2 || y02 < edgeSlop2 || x02 > rightSlop2 || y02 > bottomSlop2;
                    boolean p1sloppy2 = x12 < edgeSlop2 || y12 < edgeSlop2 || x12 > rightSlop2 || y12 > bottomSlop2;
                    if (!p0sloppy2 || !p1sloppy2) {
                        if (!p0sloppy2) {
                            if (!p1sloppy2) {
                                this.mGestureInProgress = this.mListener.onScaleBegin(this);
                                break;
                            } else {
                                this.mFocusX = event.getX(0);
                                this.mFocusY = event.getY(0);
                                this.mSloppyGesture = true;
                                break;
                            }
                        } else {
                            this.mFocusX = event.getX(1);
                            this.mFocusY = event.getY(1);
                            this.mSloppyGesture = true;
                            break;
                        }
                    } else {
                        this.mFocusX = -1.0f;
                        this.mFocusY = -1.0f;
                        this.mSloppyGesture = true;
                        break;
                    }
                case 6:
                    if (this.mSloppyGesture) {
                        int id2 = ((65280 & action) >> 8) == 0 ? 1 : 0;
                        this.mFocusX = event.getX(id2);
                        this.mFocusY = event.getY(id2);
                        break;
                    }
                    break;
            }
        }
        return true;
    }

    private static float getRawX(MotionEvent event, int pointerIndex) {
        return event.getX(pointerIndex) + (event.getX() - event.getRawX());
    }

    private static float getRawY(MotionEvent event, int pointerIndex) {
        return event.getY(pointerIndex) + (event.getY() - event.getRawY());
    }

    private void setContext(MotionEvent curr) {
        if (this.mCurrEvent != null) {
            this.mCurrEvent.recycle();
        }
        this.mCurrEvent = MotionEvent.obtain(curr);
        this.mCurrLen = -1.0f;
        this.mPrevLen = -1.0f;
        this.mScaleFactor = -1.0f;
        MotionEvent prev = this.mPrevEvent;
        float px0 = prev.getX(0);
        float py0 = prev.getY(0);
        float px1 = prev.getX(1);
        float py1 = prev.getY(1);
        float cx0 = curr.getX(0);
        float cy0 = curr.getY(0);
        float cvx = curr.getX(1) - cx0;
        float cvy = curr.getY(1) - cy0;
        this.mPrevFingerDiffX = px1 - px0;
        this.mPrevFingerDiffY = py1 - py0;
        this.mCurrFingerDiffX = cvx;
        this.mCurrFingerDiffY = cvy;
        this.mFocusX = (0.5f * cvx) + cx0;
        this.mFocusY = (0.5f * cvy) + cy0;
        this.mTimeDelta = curr.getEventTime() - prev.getEventTime();
        this.mCurrPressure = curr.getPressure(0) + curr.getPressure(1);
        this.mPrevPressure = prev.getPressure(0) + prev.getPressure(1);
    }

    private void reset() {
        if (this.mPrevEvent != null) {
            this.mPrevEvent.recycle();
            this.mPrevEvent = null;
        }
        if (this.mCurrEvent != null) {
            this.mCurrEvent.recycle();
            this.mCurrEvent = null;
        }
        this.mSloppyGesture = false;
        this.mGestureInProgress = false;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public boolean isInProgress() {
        return this.mGestureInProgress;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getFocusX() {
        return this.mFocusX;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getFocusY() {
        return this.mFocusY;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getCurrentSpan() {
        if (this.mCurrLen == -1.0f) {
            float cvx = this.mCurrFingerDiffX;
            float cvy = this.mCurrFingerDiffY;
            this.mCurrLen = (float) Math.sqrt((cvx * cvx) + (cvy * cvy));
        }
        return this.mCurrLen;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getPreviousSpan() {
        if (this.mPrevLen == -1.0f) {
            float pvx = this.mPrevFingerDiffX;
            float pvy = this.mPrevFingerDiffY;
            this.mPrevLen = (float) Math.sqrt((pvx * pvx) + (pvy * pvy));
        }
        return this.mPrevLen;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public float getScaleFactor() {
        if (this.mScaleFactor == -1.0f) {
            this.mScaleFactor = getCurrentSpan() / getPreviousSpan();
        }
        return this.mScaleFactor;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public long getTimeDelta() {
        return this.mTimeDelta;
    }

    @Override // com.antlersoft.android.p000bc.IBCScaleGestureDetector
    public long getEventTime() {
        return this.mCurrEvent.getEventTime();
    }
}
