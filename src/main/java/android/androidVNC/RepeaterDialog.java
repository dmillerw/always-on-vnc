package android.androidVNC;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

class RepeaterDialog extends Dialog {
    AndroidVNC _configurationDialog;
    private EditText _repeaterId;

    RepeaterDialog(AndroidVNC context) {
        super(context);
        setOwnerActivity(context);
        this._configurationDialog = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.repeater_dialog_title);
        setContentView(R.layout.repeater_dialog);
        this._repeaterId = (EditText) findViewById(R.id.textRepeaterInfo);
        ((TextView) findViewById(R.id.textRepeaterCaption)).setText(Html.fromHtml(getContext().getString(R.string.repeater_caption)));
        ((Button) findViewById(R.id.buttonSaveRepeater)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.RepeaterDialog.View$OnClickListenerC00321 */

            public void onClick(View v) {
                RepeaterDialog.this._configurationDialog.updateRepeaterInfo(true, RepeaterDialog.this._repeaterId.getText().toString());
                RepeaterDialog.this.dismiss();
            }
        });
        ((Button) findViewById(R.id.buttonClearRepeater)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.RepeaterDialog.View$OnClickListenerC00332 */

            public void onClick(View v) {
                RepeaterDialog.this._configurationDialog.updateRepeaterInfo(false, "");
                RepeaterDialog.this.dismiss();
            }
        });
    }
}
