package android.androidVNC;

import com.antlersoft.android.db.FieldAccessor;
import com.antlersoft.android.db.TableInterface;

@TableInterface(ImplementingClassName = "SentTextBean", ImplementingIsAbstract = false, TableName = SentTextBean.GEN_TABLE_NAME)
public interface ISentText {
    @FieldAccessor
    String getSentText();

    @FieldAccessor
    long get_Id();
}
