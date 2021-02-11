package com.antlersoft.android.bc;

import android.view.View;

/* renamed from: com.antlersoft.android.bc.BCHapticDefault */
class BCHapticDefault implements IBCHaptic {
    BCHapticDefault() {
    }

    @Override // com.antlersoft.android.p000bc.IBCHaptic
    public boolean performLongPressHaptic(View v) {
        return v.performHapticFeedback(0, 3);
    }
}
