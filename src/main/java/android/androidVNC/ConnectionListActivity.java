package android.androidVNC;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ConnectionListActivity extends ListActivity {
    VncDatabase database;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.database = new VncDatabase(this);
        Cursor mCursor = this.database.getReadableDatabase().query(AbstractConnectionBean.GEN_TABLE_NAME, new String[]{"_id", AbstractConnectionBean.GEN_FIELD_NICKNAME, AbstractConnectionBean.GEN_FIELD_USERNAME, AbstractConnectionBean.GEN_FIELD_ADDRESS, AbstractConnectionBean.GEN_FIELD_PORT, AbstractConnectionBean.GEN_FIELD_REPEATERID}, "KEEPPASSWORD <> 0", null, null, null, AbstractConnectionBean.GEN_FIELD_NICKNAME);
        startManagingCursor(mCursor);
        setListAdapter(new SimpleCursorAdapter(this, R.layout.connection_list, mCursor, new String[]{AbstractConnectionBean.GEN_FIELD_NICKNAME, AbstractConnectionBean.GEN_FIELD_ADDRESS, AbstractConnectionBean.GEN_FIELD_PORT, AbstractConnectionBean.GEN_FIELD_REPEATERID}, new int[]{R.id.list_text_nickname, R.id.list_text_address, R.id.list_text_port, R.id.list_text_repeater}));
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        ConnectionBean connection = new ConnectionBean();
        if (connection.Gen_read(this.database.getReadableDatabase(), id)) {
            Intent.ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.mouse_icon);
            Intent intent = new Intent();
            Intent launchIntent = new Intent(this, VncCanvasActivity.class);
            Uri.Builder builder = new Uri.Builder();
            builder.authority("android.androidVNC.CONNECTION:" + connection.get_Id());
            builder.scheme("vnc");
            launchIntent.setData(builder.build());
            intent.putExtra("android.intent.extra.shortcut.INTENT", launchIntent);
            intent.putExtra("android.intent.extra.shortcut.NAME", connection.getNickname());
            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", icon);
            setResult(-1, intent);
        } else {
            setResult(0);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.database.close();
        super.onDestroy();
    }
}
