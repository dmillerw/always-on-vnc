package com.antlersoft.android.bc;

import android.annotation.SuppressLint;
import android.app.ActivityManager;

@SuppressLint({"NewApi"})
/* renamed from: com.antlersoft.android.bc.BCActivityManagerV5 */
public class BCActivityManagerV5 implements IBCActivityManager {
    @Override // com.antlersoft.android.p000bc.IBCActivityManager
    @SuppressLint({"NewApi"})
    public int getMemoryClass(ActivityManager am) {
        return am.getMemoryClass();
    }
}
