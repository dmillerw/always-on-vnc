package android.androidVNC;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* access modifiers changed from: package-private */
public class VncDatabase extends SQLiteOpenHelper {
    static final int DBV_0_2_4 = 10;
    static final int DBV_0_2_X = 9;
    static final int DBV_0_4_7 = 11;
    static final int DBV_0_5_0 = 12;
    static final int DBV_0_6_0 = 13;
    public static final String TAG = VncDatabase.class.toString();

    VncDatabase(Context context) {
        super(context, "VncDatabase", (SQLiteDatabase.CursorFactory) null, 13);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AbstractConnectionBean.GEN_CREATE);
        db.execSQL(MostRecentBean.GEN_CREATE);
        db.execSQL(MetaList.GEN_CREATE);
        db.execSQL(AbstractMetaKeyBean.GEN_CREATE);
        db.execSQL(SentTextBean.GEN_CREATE);
        db.execSQL("INSERT INTO META_LIST VALUES ( 1, 'DEFAULT')");
    }

    private void defaultUpgrade(SQLiteDatabase db) {
        Log.i(TAG, "Doing default database upgrade (drop and create tables)");
        db.execSQL("DROP TABLE IF EXISTS CONNECTION_BEAN");
        db.execSQL("DROP TABLE IF EXISTS MOST_RECENT");
        db.execSQL("DROP TABLE IF EXISTS META_LIST");
        db.execSQL("DROP TABLE IF EXISTS META_KEY");
        db.execSQL("DROP TABLE IF EXISTS SENT_TEXT");
        onCreate(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
            defaultUpgrade(db);
            return;
        }
        if (oldVersion == 9) {
            Log.i(TAG, "Doing upgrade from 9 to 10");
            db.execSQL("ALTER TABLE CONNECTION_BEAN RENAME TO OLD_CONNECTION_BEAN");
            db.execSQL(AbstractConnectionBean.GEN_CREATE);
            db.execSQL("INSERT INTO CONNECTION_BEAN SELECT *, 0 FROM OLD_CONNECTION_BEAN");
            db.execSQL("DROP TABLE OLD_CONNECTION_BEAN");
            oldVersion = 10;
        }
        if (oldVersion == 10) {
            Log.i(TAG, "Doing upgrade from 10 to 11");
            db.execSQL("ALTER TABLE CONNECTION_BEAN ADD COLUMN USERNAME TEXT");
            db.execSQL("ALTER TABLE CONNECTION_BEAN ADD COLUMN SECURECONNECTIONTYPE TEXT");
            db.execSQL("ALTER TABLE MOST_RECENT ADD COLUMN SHOW_SPLASH_VERSION INTEGER");
            db.execSQL("ALTER TABLE MOST_RECENT ADD COLUMN TEXT_INDEX");
            oldVersion = 11;
        }
        if (oldVersion == 11) {
            Log.i(TAG, "Doing upgrade from 11 to 12");
            db.execSQL("DROP TABLE IF EXISTS SENT_TEXT");
            db.execSQL(SentTextBean.GEN_CREATE);
            db.execSQL("ALTER TABLE CONNECTION_BEAN ADD COLUMN SHOWZOOMBUTTONS INTEGER DEFAULT 1");
            db.execSQL("ALTER TABLE CONNECTION_BEAN ADD COLUMN DOUBLE_TAP_ACTION TEXT");
        }
        Log.i(TAG, "Doing upgrade from 12 to 13");
        db.execSQL("ALTER TABLE CONNECTION_BEAN ADD COLUMN USEIMMERSIVE INTEGER DEFAULT 1");
        db.execSQL("ALTER TABLE CONNECTION_BEAN ADD COLUMN USEWAKELOCK INTEGER");
    }
}
