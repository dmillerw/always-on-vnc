package com.antlersoft.android.bc;

import android.view.View;

/* renamed from: com.antlersoft.android.bc.BCSystemUiVisibility19 */
class BCSystemUiVisibility19 implements IBCSystemUiVisibility {
    BCSystemUiVisibility19() {
    }

    @Override // com.antlersoft.android.p000bc.IBCSystemUiVisibility
    public void HideSystemUI(View v) {
        v.setSystemUiVisibility(5894);
    }
}
