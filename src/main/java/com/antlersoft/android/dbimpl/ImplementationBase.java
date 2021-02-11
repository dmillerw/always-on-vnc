package com.antlersoft.android.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Collection;

public abstract class ImplementationBase {
    public static <E extends ImplementationBase> void Gen_populateFromCursor(Cursor cursor, Collection<E> collection, NewInstance<E> newInstance) {
        if (cursor.moveToFirst()) {
            E e = newInstance.get();
            int[] Gen_columnIndices = e.Gen_columnIndices(cursor);
            do {
                if (e == null) {
                    e = newInstance.get();
                }
                e.Gen_populate(cursor, Gen_columnIndices);
                collection.add(e);
                e = null;
            } while (cursor.moveToNext());
        }
    }

    public static <E extends ImplementationBase> void getAll(SQLiteDatabase sQLiteDatabase, String str, Collection<E> collection, NewInstance<E> newInstance) {
        Cursor query = sQLiteDatabase.query(str, null, null, null, null, null, null);
        try {
            Gen_populateFromCursor(query, collection, newInstance);
        } finally {
            query.close();
        }
    }

    public abstract int[] Gen_columnIndices(Cursor cursor);

    public abstract ContentValues Gen_getValues();

    public abstract void Gen_populate(ContentValues contentValues);

    public abstract void Gen_populate(Cursor cursor, int[] iArr);

    public abstract String Gen_tableName();
}
