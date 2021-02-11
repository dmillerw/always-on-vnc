package android.androidVNC;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import com.antlersoft.android.dbimpl.NewInstance;

/* access modifiers changed from: package-private */
public class ConnectionBean extends AbstractConnectionBean implements Comparable<ConnectionBean> {
    static final NewInstance<ConnectionBean> newInstance = new NewInstance<ConnectionBean>() {
        /* class android.androidVNC.ConnectionBean.C00011 */

        @Override // com.antlersoft.android.dbimpl.NewInstance
        public ConnectionBean get() {
            return new ConnectionBean();
        }
    };

    ConnectionBean() {
        set_Id(0);
        setAddress("");
        setPassword("");
        setKeepPassword(true);
        setNickname("");
        setUserName("");
        setPort(5900);
        setColorModel(COLORMODEL.C64.nameString());
        setScaleMode(ImageView.ScaleType.MATRIX);
        setInputMode("TOUCH_ZOOM_MODE");
        setRepeaterId("");
        setMetaListId(1);
    }

    /* access modifiers changed from: package-private */
    public boolean isNew() {
        return get_Id() == 0;
    }

    /* access modifiers changed from: package-private */
    public void save(SQLiteDatabase database) {
        ContentValues values = Gen_getValues();
        values.remove("_id");
        if (!getKeepPassword()) {
            values.put(AbstractConnectionBean.GEN_FIELD_PASSWORD, "");
        }
        if (isNew()) {
            set_Id(database.insert(AbstractConnectionBean.GEN_TABLE_NAME, null, values));
            return;
        }
        database.update(AbstractConnectionBean.GEN_TABLE_NAME, values, "_id = ?", new String[]{Long.toString(get_Id())});
    }

    /* access modifiers changed from: package-private */
    public ImageView.ScaleType getScaleMode() {
        return ImageView.ScaleType.valueOf(getScaleModeAsString());
    }

    /* access modifiers changed from: package-private */
    public void setScaleMode(ImageView.ScaleType value) {
        setScaleModeAsString(value.toString());
    }

    public String toString() {
        if (isNew()) {
            return "New";
        }
        return getNickname() + ":" + getAddress() + ":" + getPort();
    }

    public int compareTo(ConnectionBean another) {
        int result = getNickname().compareTo(another.getNickname());
        if (result != 0) {
            return result;
        }
        int result2 = getAddress().compareTo(another.getAddress());
        if (result2 == 0) {
            return getPort() - another.getPort();
        }
        return result2;
    }
}
