package com.antlersoft.android.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class IdImplementationBase extends ImplementationBase {
    private static ContentValues removeId(ContentValues contentValues) {
        contentValues.remove("_id");
        return contentValues;
    }

    public int Gen_delete(SQLiteDatabase sQLiteDatabase) {
        return sQLiteDatabase.delete(Gen_tableName(), "_id = ?", new String[]{Long.toString(get_Id())});
    }

    public boolean Gen_insert(SQLiteDatabase sQLiteDatabase) {
        long insert = sQLiteDatabase.insert(Gen_tableName(), null, removeId(Gen_getValues()));
        if (insert == -1) {
            return false;
        }
        set_Id(insert);
        return true;
    }

    public boolean Gen_read(SQLiteDatabase sQLiteDatabase, long j) {
        boolean z;
        Cursor query = sQLiteDatabase.query(Gen_tableName(), null, "_id = ?", new String[]{Long.toString(j)}, null, null, null);
        if (query.moveToFirst()) {
            Gen_populate(query, Gen_columnIndices(query));
            z = true;
        } else {
            z = false;
        }
        query.close();
        return z;
    }

    public int Gen_update(SQLiteDatabase sQLiteDatabase) {
        return sQLiteDatabase.update(Gen_tableName(), removeId(Gen_getValues()), "_id = ?", new String[]{Long.toString(get_Id())});
    }

    public abstract long get_Id();

    public abstract void set_Id(long j);
}
