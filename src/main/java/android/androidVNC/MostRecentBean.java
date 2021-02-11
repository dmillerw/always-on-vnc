package android.androidVNC;

import android.content.ContentValues;
import android.database.Cursor;
import com.antlersoft.android.dbimpl.IdImplementationBase;
import com.antlersoft.android.dbimpl.NewInstance;

public class MostRecentBean extends IdImplementationBase implements IMostRecentBean {
    public static final int GEN_COUNT = 4;
    public static String GEN_CREATE = "CREATE TABLE MOST_RECENT (_id INTEGER PRIMARY KEY AUTOINCREMENT,CONNECTION_ID INTEGER,SHOW_SPLASH_VERSION INTEGER,TEXT_INDEX INTEGER)";
    public static final String GEN_FIELD_CONNECTION_ID = "CONNECTION_ID";
    public static final String GEN_FIELD_SHOW_SPLASH_VERSION = "SHOW_SPLASH_VERSION";
    public static final String GEN_FIELD_TEXT_INDEX = "TEXT_INDEX";
    public static final String GEN_FIELD__ID = "_id";
    public static final int GEN_ID_CONNECTION_ID = 1;
    public static final int GEN_ID_SHOW_SPLASH_VERSION = 2;
    public static final int GEN_ID_TEXT_INDEX = 3;
    public static final int GEN_ID__ID = 0;
    public static final NewInstance<MostRecentBean> GEN_NEW = new NewInstance<MostRecentBean>() {
        /* class android.androidVNC.MostRecentBean.C00291 */

        @Override // com.antlersoft.android.dbimpl.NewInstance
        public MostRecentBean get() {
            return new MostRecentBean();
        }
    };
    public static final String GEN_TABLE_NAME = "MOST_RECENT";
    private long gen_CONNECTION_ID;
    private long gen_SHOW_SPLASH_VERSION;
    private long gen_TEXT_INDEX;
    private long gen__Id;

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public String Gen_tableName() {
        return GEN_TABLE_NAME;
    }

    @Override // android.androidVNC.IMostRecentBean, com.antlersoft.android.dbimpl.IdImplementationBase
    public long get_Id() {
        return this.gen__Id;
    }

    @Override // com.antlersoft.android.dbimpl.IdImplementationBase
    public void set_Id(long arg__Id) {
        this.gen__Id = arg__Id;
    }

    @Override // android.androidVNC.IMostRecentBean
    public long getConnectionId() {
        return this.gen_CONNECTION_ID;
    }

    public void setConnectionId(long arg_CONNECTION_ID) {
        this.gen_CONNECTION_ID = arg_CONNECTION_ID;
    }

    @Override // android.androidVNC.IMostRecentBean
    public long getShowSplashVersion() {
        return this.gen_SHOW_SPLASH_VERSION;
    }

    public void setShowSplashVersion(long arg_SHOW_SPLASH_VERSION) {
        this.gen_SHOW_SPLASH_VERSION = arg_SHOW_SPLASH_VERSION;
    }

    @Override // android.androidVNC.IMostRecentBean
    public long getTextIndex() {
        return this.gen_TEXT_INDEX;
    }

    public void setTextIndex(long arg_TEXT_INDEX) {
        this.gen_TEXT_INDEX = arg_TEXT_INDEX;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public ContentValues Gen_getValues() {
        ContentValues values = new ContentValues();
        values.put("_id", Long.toString(this.gen__Id));
        values.put(GEN_FIELD_CONNECTION_ID, Long.toString(this.gen_CONNECTION_ID));
        values.put(GEN_FIELD_SHOW_SPLASH_VERSION, Long.toString(this.gen_SHOW_SPLASH_VERSION));
        values.put(GEN_FIELD_TEXT_INDEX, Long.toString(this.gen_TEXT_INDEX));
        return values;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public int[] Gen_columnIndices(Cursor cursor) {
        int[] result = new int[4];
        result[0] = cursor.getColumnIndex("_id");
        if (result[0] == -1) {
            result[0] = cursor.getColumnIndex("_ID");
        }
        result[1] = cursor.getColumnIndex(GEN_FIELD_CONNECTION_ID);
        result[2] = cursor.getColumnIndex(GEN_FIELD_SHOW_SPLASH_VERSION);
        result[3] = cursor.getColumnIndex(GEN_FIELD_TEXT_INDEX);
        return result;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(Cursor cursor, int[] columnIndices) {
        if (columnIndices[0] >= 0 && !cursor.isNull(columnIndices[0])) {
            this.gen__Id = cursor.getLong(columnIndices[0]);
        }
        if (columnIndices[1] >= 0 && !cursor.isNull(columnIndices[1])) {
            this.gen_CONNECTION_ID = cursor.getLong(columnIndices[1]);
        }
        if (columnIndices[2] >= 0 && !cursor.isNull(columnIndices[2])) {
            this.gen_SHOW_SPLASH_VERSION = cursor.getLong(columnIndices[2]);
        }
        if (columnIndices[3] >= 0 && !cursor.isNull(columnIndices[3])) {
            this.gen_TEXT_INDEX = cursor.getLong(columnIndices[3]);
        }
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(ContentValues values) {
        this.gen__Id = values.getAsLong("_id").longValue();
        this.gen_CONNECTION_ID = values.getAsLong(GEN_FIELD_CONNECTION_ID).longValue();
        this.gen_SHOW_SPLASH_VERSION = values.getAsLong(GEN_FIELD_SHOW_SPLASH_VERSION).longValue();
        this.gen_TEXT_INDEX = values.getAsLong(GEN_FIELD_TEXT_INDEX).longValue();
    }
}
