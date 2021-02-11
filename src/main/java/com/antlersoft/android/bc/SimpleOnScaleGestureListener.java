package com.antlersoft.android.bc;

/* renamed from: com.antlersoft.android.bc.SimpleOnScaleGestureListener */
public class SimpleOnScaleGestureListener implements OnScaleGestureListener {
    @Override // com.antlersoft.android.p000bc.OnScaleGestureListener
    public boolean onScale(IBCScaleGestureDetector detector) {
        return false;
    }

    @Override // com.antlersoft.android.p000bc.OnScaleGestureListener
    public boolean onScaleBegin(IBCScaleGestureDetector detector) {
        return true;
    }

    @Override // com.antlersoft.android.p000bc.OnScaleGestureListener
    public void onScaleEnd(IBCScaleGestureDetector detector) {
    }
}
