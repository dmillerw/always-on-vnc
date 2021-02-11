package com.antlersoft.android.bc;

import android.app.ActivityManager;

/* access modifiers changed from: package-private */
/* renamed from: com.antlersoft.android.bc.BCActivityManagerDefault */
public class BCActivityManagerDefault implements IBCActivityManager {
    BCActivityManagerDefault() {
    }

    @Override // com.antlersoft.android.p000bc.IBCActivityManager
    public int getMemoryClass(ActivityManager am) {
        return 16;
    }
}
