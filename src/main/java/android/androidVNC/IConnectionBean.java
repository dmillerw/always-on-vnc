package android.androidVNC;

import com.antlersoft.android.db.FieldAccessor;
import com.antlersoft.android.db.TableInterface;

@TableInterface(ImplementingClassName = "AbstractConnectionBean", TableName = AbstractConnectionBean.GEN_TABLE_NAME)
interface IConnectionBean {
    @FieldAccessor
    String getAddress();

    @FieldAccessor
    String getColorModel();

    @FieldAccessor(Name = AbstractConnectionBean.GEN_FIELD_DOUBLE_TAP_ACTION)
    String getDoubleTapActionAsString();

    @FieldAccessor
    boolean getFollowMouse();

    @FieldAccessor(DefaultValue = "false")
    boolean getFollowPan();

    @FieldAccessor
    long getForceFull();

    @FieldAccessor
    String getInputMode();

    @FieldAccessor
    boolean getKeepPassword();

    @FieldAccessor(Name = AbstractConnectionBean.GEN_FIELD_LAST_META_KEY_ID)
    long getLastMetaKeyId();

    @FieldAccessor
    long getMetaListId();

    @FieldAccessor
    String getNickname();

    @FieldAccessor
    String getPassword();

    @FieldAccessor
    int getPort();

    @FieldAccessor
    String getRepeaterId();

    @FieldAccessor(Name = AbstractConnectionBean.GEN_FIELD_SCALEMODE)
    String getScaleModeAsString();

    @FieldAccessor
    String getSecureConnectionType();

    @FieldAccessor(DefaultValue = "true")
    boolean getShowZoomButtons();

    @FieldAccessor(DefaultValue = "true")
    boolean getUseImmersive();

    @FieldAccessor
    boolean getUseLocalCursor();

    @FieldAccessor
    boolean getUseRepeater();

    @FieldAccessor(DefaultValue = "false")
    boolean getUseWakeLock();

    @FieldAccessor
    String getUserName();

    @FieldAccessor
    long get_Id();
}
