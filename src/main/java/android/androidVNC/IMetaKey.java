package android.androidVNC;

import com.antlersoft.android.db.FieldAccessor;
import com.antlersoft.android.db.TableInterface;

@TableInterface(ImplementingClassName = "AbstractMetaKeyBean", TableName = AbstractMetaKeyBean.GEN_TABLE_NAME)
public interface IMetaKey {
    @FieldAccessor
    String getKeyDesc();

    @FieldAccessor
    int getKeySym();

    @FieldAccessor
    int getMetaFlags();

    @FieldAccessor
    long getMetaListId();

    @FieldAccessor
    int getMouseButtons();

    @FieldAccessor
    String getShortcut();

    @FieldAccessor
    long get_Id();

    @FieldAccessor
    boolean isMouseClick();
}
