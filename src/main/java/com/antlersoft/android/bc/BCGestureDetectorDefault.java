package com.antlersoft.android.bc;

import android.content.Context;
import android.view.GestureDetector;

/* renamed from: com.antlersoft.android.bc.BCGestureDetectorDefault */
public class BCGestureDetectorDefault implements IBCGestureDetector {
    @Override // com.antlersoft.android.p000bc.IBCGestureDetector
    public GestureDetector createGestureDetector(Context context, GestureDetector.OnGestureListener listener) {
        return new GestureDetector(context, listener);
    }
}
