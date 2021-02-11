package com.antlersoft.android.bc;

import android.view.MotionEvent;

/* renamed from: com.antlersoft.android.bc.BCMotionEvent4 */
class BCMotionEvent4 implements IBCMotionEvent {
    BCMotionEvent4() {
    }

    @Override // com.antlersoft.android.p000bc.IBCMotionEvent
    public int getPointerCount(MotionEvent evt) {
        return 1;
    }
}
