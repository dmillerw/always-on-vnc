package android.androidVNC;

import android.content.ContentValues;
import android.database.Cursor;
import com.antlersoft.android.dbimpl.IdImplementationBase;

public abstract class AbstractMetaKeyBean extends IdImplementationBase implements IMetaKey {
    public static final int GEN_COUNT = 8;
    public static String GEN_CREATE = "CREATE TABLE META_KEY (_id INTEGER PRIMARY KEY AUTOINCREMENT,METALISTID INTEGER,KEYDESC TEXT,METAFLAGS INTEGER,MOUSECLICK INTEGER,MOUSEBUTTONS INTEGER,KEYSYM INTEGER,SHORTCUT TEXT)";
    public static final String GEN_FIELD_KEYDESC = "KEYDESC";
    public static final String GEN_FIELD_KEYSYM = "KEYSYM";
    public static final String GEN_FIELD_METAFLAGS = "METAFLAGS";
    public static final String GEN_FIELD_METALISTID = "METALISTID";
    public static final String GEN_FIELD_MOUSEBUTTONS = "MOUSEBUTTONS";
    public static final String GEN_FIELD_MOUSECLICK = "MOUSECLICK";
    public static final String GEN_FIELD_SHORTCUT = "SHORTCUT";
    public static final String GEN_FIELD__ID = "_id";
    public static final int GEN_ID_KEYDESC = 2;
    public static final int GEN_ID_KEYSYM = 6;
    public static final int GEN_ID_METAFLAGS = 3;
    public static final int GEN_ID_METALISTID = 1;
    public static final int GEN_ID_MOUSEBUTTONS = 5;
    public static final int GEN_ID_MOUSECLICK = 4;
    public static final int GEN_ID_SHORTCUT = 7;
    public static final int GEN_ID__ID = 0;
    public static final String GEN_TABLE_NAME = "META_KEY";
    private long gen__Id;
    private String gen_keyDesc;
    private int gen_keySym;
    private int gen_metaFlags;
    private long gen_metaListId;
    private int gen_mouseButtons;
    private boolean gen_mouseClick;
    private String gen_shortcut;

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public String Gen_tableName() {
        return GEN_TABLE_NAME;
    }

    @Override // android.androidVNC.IMetaKey, com.antlersoft.android.dbimpl.IdImplementationBase
    public long get_Id() {
        return this.gen__Id;
    }

    @Override // com.antlersoft.android.dbimpl.IdImplementationBase
    public void set_Id(long arg__Id) {
        this.gen__Id = arg__Id;
    }

    @Override // android.androidVNC.IMetaKey
    public long getMetaListId() {
        return this.gen_metaListId;
    }

    public void setMetaListId(long arg_metaListId) {
        this.gen_metaListId = arg_metaListId;
    }

    @Override // android.androidVNC.IMetaKey
    public String getKeyDesc() {
        return this.gen_keyDesc;
    }

    public void setKeyDesc(String arg_keyDesc) {
        this.gen_keyDesc = arg_keyDesc;
    }

    @Override // android.androidVNC.IMetaKey
    public int getMetaFlags() {
        return this.gen_metaFlags;
    }

    public void setMetaFlags(int arg_metaFlags) {
        this.gen_metaFlags = arg_metaFlags;
    }

    @Override // android.androidVNC.IMetaKey
    public boolean isMouseClick() {
        return this.gen_mouseClick;
    }

    public void setMouseClick(boolean arg_mouseClick) {
        this.gen_mouseClick = arg_mouseClick;
    }

    @Override // android.androidVNC.IMetaKey
    public int getMouseButtons() {
        return this.gen_mouseButtons;
    }

    public void setMouseButtons(int arg_mouseButtons) {
        this.gen_mouseButtons = arg_mouseButtons;
    }

    @Override // android.androidVNC.IMetaKey
    public int getKeySym() {
        return this.gen_keySym;
    }

    public void setKeySym(int arg_keySym) {
        this.gen_keySym = arg_keySym;
    }

    @Override // android.androidVNC.IMetaKey
    public String getShortcut() {
        return this.gen_shortcut;
    }

    public void setShortcut(String arg_shortcut) {
        this.gen_shortcut = arg_shortcut;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public ContentValues Gen_getValues() {
        ContentValues values = new ContentValues();
        values.put("_id", Long.toString(this.gen__Id));
        values.put("METALISTID", Long.toString(this.gen_metaListId));
        values.put(GEN_FIELD_KEYDESC, this.gen_keyDesc);
        values.put(GEN_FIELD_METAFLAGS, Integer.toString(this.gen_metaFlags));
        values.put(GEN_FIELD_MOUSECLICK, this.gen_mouseClick ? "1" : "0");
        values.put(GEN_FIELD_MOUSEBUTTONS, Integer.toString(this.gen_mouseButtons));
        values.put(GEN_FIELD_KEYSYM, Integer.toString(this.gen_keySym));
        values.put(GEN_FIELD_SHORTCUT, this.gen_shortcut);
        return values;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public int[] Gen_columnIndices(Cursor cursor) {
        int[] result = new int[8];
        result[0] = cursor.getColumnIndex("_id");
        if (result[0] == -1) {
            result[0] = cursor.getColumnIndex("_ID");
        }
        result[1] = cursor.getColumnIndex("METALISTID");
        result[2] = cursor.getColumnIndex(GEN_FIELD_KEYDESC);
        result[3] = cursor.getColumnIndex(GEN_FIELD_METAFLAGS);
        result[4] = cursor.getColumnIndex(GEN_FIELD_MOUSECLICK);
        result[5] = cursor.getColumnIndex(GEN_FIELD_MOUSEBUTTONS);
        result[6] = cursor.getColumnIndex(GEN_FIELD_KEYSYM);
        result[7] = cursor.getColumnIndex(GEN_FIELD_SHORTCUT);
        return result;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(Cursor cursor, int[] columnIndices) {
        boolean z = true;
        if (columnIndices[0] >= 0 && !cursor.isNull(columnIndices[0])) {
            this.gen__Id = cursor.getLong(columnIndices[0]);
        }
        if (columnIndices[1] >= 0 && !cursor.isNull(columnIndices[1])) {
            this.gen_metaListId = cursor.getLong(columnIndices[1]);
        }
        if (columnIndices[2] >= 0 && !cursor.isNull(columnIndices[2])) {
            this.gen_keyDesc = cursor.getString(columnIndices[2]);
        }
        if (columnIndices[3] >= 0 && !cursor.isNull(columnIndices[3])) {
            this.gen_metaFlags = cursor.getInt(columnIndices[3]);
        }
        if (columnIndices[4] >= 0 && !cursor.isNull(columnIndices[4])) {
            if (cursor.getInt(columnIndices[4]) == 0) {
                z = false;
            }
            this.gen_mouseClick = z;
        }
        if (columnIndices[5] >= 0 && !cursor.isNull(columnIndices[5])) {
            this.gen_mouseButtons = cursor.getInt(columnIndices[5]);
        }
        if (columnIndices[6] >= 0 && !cursor.isNull(columnIndices[6])) {
            this.gen_keySym = cursor.getInt(columnIndices[6]);
        }
        if (columnIndices[7] >= 0 && !cursor.isNull(columnIndices[7])) {
            this.gen_shortcut = cursor.getString(columnIndices[7]);
        }
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(ContentValues values) {
        this.gen__Id = values.getAsLong("_id").longValue();
        this.gen_metaListId = values.getAsLong("METALISTID").longValue();
        this.gen_keyDesc = values.getAsString(GEN_FIELD_KEYDESC);
        this.gen_metaFlags = values.getAsInteger(GEN_FIELD_METAFLAGS).intValue();
        this.gen_mouseClick = values.getAsInteger(GEN_FIELD_MOUSECLICK).intValue() != 0;
        this.gen_mouseButtons = values.getAsInteger(GEN_FIELD_MOUSEBUTTONS).intValue();
        this.gen_keySym = values.getAsInteger(GEN_FIELD_KEYSYM).intValue();
        this.gen_shortcut = values.getAsString(GEN_FIELD_SHORTCUT);
    }
}
