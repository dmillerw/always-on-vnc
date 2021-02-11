package com.antlersoft.android.bc;

import android.content.Context;
import java.io.File;

/* renamed from: com.antlersoft.android.bc.BCStorageContext8 */
class BCStorageContext8 implements IBCStorageContext {
    BCStorageContext8() {
    }

    @Override // com.antlersoft.android.p000bc.IBCStorageContext
    public File getExternalStorageDir(Context context, String type) {
        return context.getExternalFilesDir(type);
    }
}
