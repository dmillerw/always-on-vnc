package android.androidVNC;

import android.content.ContentValues;
import android.database.Cursor;
import com.antlersoft.android.dbimpl.IdImplementationBase;
import com.antlersoft.android.dbimpl.NewInstance;

public class MetaList extends IdImplementationBase implements IMetaList {
    public static final int GEN_COUNT = 2;
    public static String GEN_CREATE = "CREATE TABLE META_LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)";
    public static final String GEN_FIELD_NAME = "NAME";
    public static final String GEN_FIELD__ID = "_id";
    public static final int GEN_ID_NAME = 1;
    public static final int GEN_ID__ID = 0;
    public static final NewInstance<MetaList> GEN_NEW = new NewInstance<MetaList>() {
        /* class android.androidVNC.MetaList.C00281 */

        @Override // com.antlersoft.android.dbimpl.NewInstance
        public MetaList get() {
            return new MetaList();
        }
    };
    public static final String GEN_TABLE_NAME = "META_LIST";
    private long gen__Id;
    private String gen_name;

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public String Gen_tableName() {
        return GEN_TABLE_NAME;
    }

    @Override // android.androidVNC.IMetaList, com.antlersoft.android.dbimpl.IdImplementationBase
    public long get_Id() {
        return this.gen__Id;
    }

    @Override // com.antlersoft.android.dbimpl.IdImplementationBase
    public void set_Id(long arg__Id) {
        this.gen__Id = arg__Id;
    }

    @Override // android.androidVNC.IMetaList
    public String getName() {
        return this.gen_name;
    }

    public void setName(String arg_name) {
        this.gen_name = arg_name;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public ContentValues Gen_getValues() {
        ContentValues values = new ContentValues();
        values.put("_id", Long.toString(this.gen__Id));
        values.put(GEN_FIELD_NAME, this.gen_name);
        return values;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public int[] Gen_columnIndices(Cursor cursor) {
        int[] result = new int[2];
        result[0] = cursor.getColumnIndex("_id");
        if (result[0] == -1) {
            result[0] = cursor.getColumnIndex("_ID");
        }
        result[1] = cursor.getColumnIndex(GEN_FIELD_NAME);
        return result;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(Cursor cursor, int[] columnIndices) {
        if (columnIndices[0] >= 0 && !cursor.isNull(columnIndices[0])) {
            this.gen__Id = cursor.getLong(columnIndices[0]);
        }
        if (columnIndices[1] >= 0 && !cursor.isNull(columnIndices[1])) {
            this.gen_name = cursor.getString(columnIndices[1]);
        }
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(ContentValues values) {
        this.gen__Id = values.getAsLong("_id").longValue();
        this.gen_name = values.getAsString(GEN_FIELD_NAME);
    }
}
