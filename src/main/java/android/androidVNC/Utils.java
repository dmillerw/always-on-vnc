package android.androidVNC;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

public class Utils {
    private static final Intent docIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://code.google.com/p/android-vnc-viewer/wiki/Documentation"));
    private static int nextNoticeID = 0;

    public static void showYesNoPrompt(Context _context, String title, String message, DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setIcon(17301659);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", onYesListener);
        builder.setNegativeButton("No", onNoListener);
        builder.show();
    }

    public static ActivityManager getActivityManager(Context context) {
        ActivityManager result = (ActivityManager) context.getSystemService("activity");
        if (result != null) {
            return result;
        }
        throw new UnsupportedOperationException("Could not retrieve ActivityManager");
    }

    public static ActivityManager.MemoryInfo getMemoryInfo(Context _context) {
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        getActivityManager(_context).getMemoryInfo(info);
        return info;
    }

    public static void showDocumentation(Context c) {
        c.startActivity(docIntent);
    }

    public static int nextNoticeID() {
        nextNoticeID++;
        return nextNoticeID;
    }

    public static void showErrorMessage(Context _context, String message) {
        showMessage(_context, "Error!", message, 17301543, null);
    }

    public static void showFatalErrorMessage(final Context _context, String message) {
        showMessage(_context, "Error!", message, 17301543, new DialogInterface.OnClickListener() {
            /* class android.androidVNC.Utils.DialogInterface$OnClickListenerC00351 */

            public void onClick(DialogInterface dialog, int which) {
                ((Activity) _context).finish();
            }
        });
    }

    public static void showMessage(Context _context, String title, String message, int icon, DialogInterface.OnClickListener ackHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(false);
        builder.setPositiveButton("Acknowledged", ackHandler);
        builder.setIcon(icon);
        builder.show();
    }
}
