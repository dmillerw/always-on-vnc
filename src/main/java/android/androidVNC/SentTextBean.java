package android.androidVNC;

import android.content.ContentValues;
import android.database.Cursor;
import com.antlersoft.android.dbimpl.IdImplementationBase;
import com.antlersoft.android.dbimpl.NewInstance;

public class SentTextBean extends IdImplementationBase implements ISentText {
    public static final int GEN_COUNT = 2;
    public static String GEN_CREATE = "CREATE TABLE SENT_TEXT (_id INTEGER PRIMARY KEY AUTOINCREMENT,SENTTEXT TEXT)";
    public static final String GEN_FIELD_SENTTEXT = "SENTTEXT";
    public static final String GEN_FIELD__ID = "_id";
    public static final int GEN_ID_SENTTEXT = 1;
    public static final int GEN_ID__ID = 0;
    public static final NewInstance<SentTextBean> GEN_NEW = new NewInstance<SentTextBean>() {
        /* class android.androidVNC.SentTextBean.C00341 */

        @Override // com.antlersoft.android.dbimpl.NewInstance
        public SentTextBean get() {
            return new SentTextBean();
        }
    };
    public static final String GEN_TABLE_NAME = "SENT_TEXT";
    private long gen__Id;
    private String gen_sentText;

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public String Gen_tableName() {
        return GEN_TABLE_NAME;
    }

    @Override // android.androidVNC.ISentText, com.antlersoft.android.dbimpl.IdImplementationBase
    public long get_Id() {
        return this.gen__Id;
    }

    @Override // com.antlersoft.android.dbimpl.IdImplementationBase
    public void set_Id(long arg__Id) {
        this.gen__Id = arg__Id;
    }

    @Override // android.androidVNC.ISentText
    public String getSentText() {
        return this.gen_sentText;
    }

    public void setSentText(String arg_sentText) {
        this.gen_sentText = arg_sentText;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public ContentValues Gen_getValues() {
        ContentValues values = new ContentValues();
        values.put("_id", Long.toString(this.gen__Id));
        values.put(GEN_FIELD_SENTTEXT, this.gen_sentText);
        return values;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public int[] Gen_columnIndices(Cursor cursor) {
        int[] result = new int[2];
        result[0] = cursor.getColumnIndex("_id");
        if (result[0] == -1) {
            result[0] = cursor.getColumnIndex("_ID");
        }
        result[1] = cursor.getColumnIndex(GEN_FIELD_SENTTEXT);
        return result;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(Cursor cursor, int[] columnIndices) {
        if (columnIndices[0] >= 0 && !cursor.isNull(columnIndices[0])) {
            this.gen__Id = cursor.getLong(columnIndices[0]);
        }
        if (columnIndices[1] >= 0 && !cursor.isNull(columnIndices[1])) {
            this.gen_sentText = cursor.getString(columnIndices[1]);
        }
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(ContentValues values) {
        this.gen__Id = values.getAsLong("_id").longValue();
        this.gen_sentText = values.getAsString(GEN_FIELD_SENTTEXT);
    }
}
