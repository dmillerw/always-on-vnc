package android.androidVNC;

import com.antlersoft.android.db.FieldAccessor;
import com.antlersoft.android.db.TableInterface;

@TableInterface(ImplementingClassName = "MetaList", ImplementingIsAbstract = false, TableName = MetaList.GEN_TABLE_NAME)
public interface IMetaList {
    @FieldAccessor
    String getName();

    @FieldAccessor
    long get_Id();
}
