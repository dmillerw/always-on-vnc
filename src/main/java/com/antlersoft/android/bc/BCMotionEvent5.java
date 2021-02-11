package com.antlersoft.android.bc;

import android.view.MotionEvent;

/* renamed from: com.antlersoft.android.bc.BCMotionEvent5 */
class BCMotionEvent5 implements IBCMotionEvent {
    BCMotionEvent5() {
    }

    @Override // com.antlersoft.android.p000bc.IBCMotionEvent
    public int getPointerCount(MotionEvent evt) {
        return evt.getPointerCount();
    }
}
