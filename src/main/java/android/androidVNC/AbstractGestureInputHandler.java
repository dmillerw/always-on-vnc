package android.androidVNC;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.antlersoft.android.bc.BCFactory;
import com.antlersoft.android.bc.IBCScaleGestureDetector;
import com.antlersoft.android.bc.OnScaleGestureListener;

abstract class AbstractGestureInputHandler extends GestureDetector.SimpleOnGestureListener implements AbstractInputHandler, OnScaleGestureListener {
    private static final String TAG = "AbstractGestureInputHandler";
    private VncCanvasActivity activity;
    protected GestureDetector gestures;
    boolean inScaling;
    protected IBCScaleGestureDetector scaleGestures;
    float xInitialFocus;
    float yInitialFocus;

    AbstractGestureInputHandler(VncCanvasActivity c) {
        this.activity = c;
        this.gestures = BCFactory.getInstance().getBCGestureDetector().createGestureDetector(c, this);
        this.gestures.setOnDoubleTapListener(this);
        this.scaleGestures = BCFactory.getInstance().getScaleGestureDetector(c, this);
    }

    @Override // android.androidVNC.AbstractInputHandler
    public boolean onTouchEvent(MotionEvent evt) {
        this.scaleGestures.onTouchEvent(evt);
        return this.gestures.onTouchEvent(evt);
    }

    @Override // com.antlersoft.android.p000bc.OnScaleGestureListener
    public boolean onScale(IBCScaleGestureDetector detector) {
        boolean consumed = true;
        float fx = detector.getFocusX();
        float fy = detector.getFocusY();
        double xfs = (double) (fx - this.xInitialFocus);
        double yfs = (double) (fy - this.yInitialFocus);
        double fs = Math.sqrt((xfs * xfs) + (yfs * yfs));
        if (Math.abs(1.0d - ((double) detector.getScaleFactor())) < 0.02d) {
            consumed = false;
        }
        if (2.0d * fs < ((double) Math.abs(detector.getCurrentSpan() - detector.getPreviousSpan()))) {
            this.inScaling = true;
            if (!(!consumed || this.activity.vncCanvas == null || this.activity.vncCanvas.scaling == null)) {
                this.activity.vncCanvas.scaling.adjust(this.activity, detector.getScaleFactor(), fx, fy);
            }
        }
        return consumed;
    }

    @Override // com.antlersoft.android.p000bc.OnScaleGestureListener
    public boolean onScaleBegin(IBCScaleGestureDetector detector) {
        this.xInitialFocus = detector.getFocusX();
        this.yInitialFocus = detector.getFocusY();
        this.inScaling = false;
        return true;
    }

    @Override // com.antlersoft.android.p000bc.OnScaleGestureListener
    public void onScaleEnd(IBCScaleGestureDetector detector) {
        this.inScaling = false;
    }
}
