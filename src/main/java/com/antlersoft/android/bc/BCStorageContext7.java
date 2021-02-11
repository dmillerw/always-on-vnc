package com.antlersoft.android.bc;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/* renamed from: com.antlersoft.android.bc.BCStorageContext7 */
public class BCStorageContext7 implements IBCStorageContext {
    @Override // com.antlersoft.android.p000bc.IBCStorageContext
    public File getExternalStorageDir(Context context, String type) {
        File f;
        File f2 = new File(Environment.getExternalStorageDirectory(), "Android/data/android.androidVNC/files");
        if (type != null) {
            f = new File(f2, type);
        } else {
            f = f2;
        }
        f.mkdirs();
        return f;
    }
}
