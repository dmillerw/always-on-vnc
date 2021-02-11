package android.androidVNC;

import com.antlersoft.android.db.FieldAccessor;
import com.antlersoft.android.db.TableInterface;

@TableInterface(ImplementingClassName = "MostRecentBean", ImplementingIsAbstract = false, TableName = MostRecentBean.GEN_TABLE_NAME)
public interface IMostRecentBean {
    @FieldAccessor(Name = MostRecentBean.GEN_FIELD_CONNECTION_ID)
    long getConnectionId();

    @FieldAccessor(Name = MostRecentBean.GEN_FIELD_SHOW_SPLASH_VERSION)
    long getShowSplashVersion();

    @FieldAccessor(Name = MostRecentBean.GEN_FIELD_TEXT_INDEX)
    long getTextIndex();

    @FieldAccessor
    long get_Id();
}
