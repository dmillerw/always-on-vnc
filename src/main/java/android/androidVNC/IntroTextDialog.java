package android.androidVNC;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/* access modifiers changed from: package-private */
public class IntroTextDialog extends Dialog {
    static IntroTextDialog dialog;
    private VncDatabase database;
    private PackageInfo packageInfo;

    static void showIntroTextIfNecessary(Activity context, VncDatabase database2) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("android.androidVNC", 0);
            MostRecentBean mr = AndroidVNC.getMostRecent(database2.getReadableDatabase());
            if ((mr == null || mr.getShowSplashVersion() != ((long) pi.versionCode)) && dialog == null) {
                dialog = new IntroTextDialog(context, pi, database2);
                dialog.show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    private IntroTextDialog(Activity context, PackageInfo pi, VncDatabase database2) {
        super(context);
        setOwnerActivity(context);
        this.packageInfo = pi;
        this.database = database2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_dialog);
        StringBuilder sb = new StringBuilder(getContext().getResources().getString(R.string.intro_title));
        sb.append(" ");
        sb.append(this.packageInfo.versionName);
        setTitle(sb);
        sb.delete(0, sb.length());
        sb.append(getContext().getResources().getString(R.string.intro_text));
        sb.append(this.packageInfo.versionName);
        sb.append(getContext().getResources().getString(R.string.intro_version_text));
        TextView introTextView = (TextView) findViewById(R.id.textIntroText);
        introTextView.setText(Html.fromHtml(sb.toString()));
        introTextView.setMovementMethod(LinkMovementMethod.getInstance());
        ((Button) findViewById(R.id.buttonCloseIntro)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.IntroTextDialog.View$OnClickListenerC00111 */

            public void onClick(View v) {
                IntroTextDialog.this.dismiss();
            }
        });
        ((Button) findViewById(R.id.buttonCloseIntroDontShow)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.IntroTextDialog.View$OnClickListenerC00122 */

            public void onClick(View v) {
                IntroTextDialog.this.dontShowAgain();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getOwnerActivity().getMenuInflater().inflate(R.menu.intro_dialog_menu, menu);
        menu.findItem(R.id.itemOpenDoc).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            /* class android.androidVNC.IntroTextDialog.MenuItem$OnMenuItemClickListenerC00133 */

            public boolean onMenuItemClick(MenuItem item) {
                Utils.showDocumentation(IntroTextDialog.this.getOwnerActivity());
                IntroTextDialog.this.dismiss();
                return true;
            }
        });
        menu.findItem(R.id.itemClose).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            /* class android.androidVNC.IntroTextDialog.MenuItem$OnMenuItemClickListenerC00144 */

            public boolean onMenuItemClick(MenuItem item) {
                IntroTextDialog.this.dismiss();
                return true;
            }
        });
        menu.findItem(R.id.itemDontShowAgain).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            /* class android.androidVNC.IntroTextDialog.MenuItem$OnMenuItemClickListenerC00155 */

            public boolean onMenuItemClick(MenuItem item) {
                IntroTextDialog.this.dontShowAgain();
                return true;
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void dontShowAgain() {
        SQLiteDatabase db = this.database.getWritableDatabase();
        MostRecentBean mostRecent = AndroidVNC.getMostRecent(db);
        if (mostRecent != null) {
            mostRecent.setShowSplashVersion((long) this.packageInfo.versionCode);
            mostRecent.Gen_update(db);
        }
        dismiss();
    }
}
