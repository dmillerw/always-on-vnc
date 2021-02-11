package com.antlersoft.android.bc;

import android.content.Context;
import android.view.GestureDetector;

/* renamed from: com.antlersoft.android.bc.IBCGestureDetector */
public interface IBCGestureDetector {
    GestureDetector createGestureDetector(Context context, GestureDetector.OnGestureListener onGestureListener);
}
