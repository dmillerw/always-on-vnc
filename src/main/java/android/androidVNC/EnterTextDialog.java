package android.androidVNC;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.io.IOException;
import java.util.ArrayList;

class EnterTextDialog extends Dialog {
    static final int DELETED_ID = -10;
    static final int NUMBER_SENT_SAVED = 100;
    private ImageButton _buttonNextEntry;
    private ImageButton _buttonPreviousEntry;
    private VncCanvasActivity _canvasActivity;
    private ArrayList<SentTextBean> _history = new ArrayList<>();
    private int _historyIndex;
    private EditText _textEnterText;

    static /* synthetic */ int access$108(EnterTextDialog x0) {
        int i = x0._historyIndex;
        x0._historyIndex = i + 1;
        return i;
    }

    public EnterTextDialog(Context context) {
        super(context);
        setOwnerActivity((Activity) context);
        this._canvasActivity = (VncCanvasActivity) context;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String saveText(boolean wasSent) {
        CharSequence cs = this._textEnterText.getText();
        if (cs.length() == 0) {
            return "";
        }
        String s = cs.toString();
        if (!wasSent && this._historyIndex < this._history.size() && s.equals(this._history.get(this._historyIndex).getSentText())) {
            return s;
        }
        SentTextBean added = new SentTextBean();
        added.setSentText(s);
        SQLiteDatabase db = this._canvasActivity.database.getWritableDatabase();
        added.Gen_insert(db);
        this._history.add(added);
        for (int i = 0; i < this._historyIndex - 100; i++) {
            SentTextBean deleteCandidate = this._history.get(i);
            if (deleteCandidate.get_Id() != -10) {
                deleteCandidate.Gen_delete(db);
                deleteCandidate.set_Id(-10);
            }
        }
        return s;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendText(String s) {
        RfbProto rfb = this._canvasActivity.vncCanvas.rfb;
        int l = s.length();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            int keysym = c;
            if (Character.isISOControl(c)) {
                if (c == '\n') {
                    keysym = MetaKeyBean.keysByKeyCode.get(66).keySym;
                }
            }
            try {
                rfb.writeKeyEvent(keysym, 0, true);
                rfb.writeKeyEvent(keysym, 0, false);
            } catch (IOException e) {
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entertext);
        setTitle(R.string.enter_text_title);
        this._textEnterText = (EditText) findViewById(R.id.textEnterText);
        this._buttonNextEntry = (ImageButton) findViewById(R.id.buttonNextEntry);
        this._buttonNextEntry.setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.EnterTextDialog.View$OnClickListenerC00041 */

            public void onClick(View v) {
                int oldSize = EnterTextDialog.this._history.size();
                if (EnterTextDialog.this._historyIndex < oldSize) {
                    EnterTextDialog.this.saveText(false);
                    EnterTextDialog.access$108(EnterTextDialog.this);
                    if (EnterTextDialog.this._history.size() > oldSize && EnterTextDialog.this._historyIndex == oldSize) {
                        EnterTextDialog.access$108(EnterTextDialog.this);
                    }
                    if (EnterTextDialog.this._historyIndex < EnterTextDialog.this._history.size()) {
                        EnterTextDialog.this._textEnterText.setText(((SentTextBean) EnterTextDialog.this._history.get(EnterTextDialog.this._historyIndex)).getSentText());
                    } else {
                        EnterTextDialog.this._textEnterText.setText("");
                    }
                }
                EnterTextDialog.this.updateButtons();
            }
        });
        this._buttonPreviousEntry = (ImageButton) findViewById(R.id.buttonPreviousEntry);
        this._buttonPreviousEntry.setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.EnterTextDialog.View$OnClickListenerC00052 */

            public void onClick(View v) {
                if (EnterTextDialog.this._historyIndex > 0) {
                    EnterTextDialog.this.saveText(false);
                    EnterTextDialog.access$108(EnterTextDialog.this);
                    EnterTextDialog.this._textEnterText.setText(((SentTextBean) EnterTextDialog.this._history.get(EnterTextDialog.this._historyIndex)).getSentText());
                }
                EnterTextDialog.this.updateButtons();
            }
        });
        ((Button) findViewById(R.id.buttonSendText)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.EnterTextDialog.View$OnClickListenerC00063 */

            public void onClick(View v) {
                EnterTextDialog.this.sendText(EnterTextDialog.this.saveText(true));
                EnterTextDialog.this._textEnterText.setText("");
                EnterTextDialog.this._historyIndex = EnterTextDialog.this._history.size();
                EnterTextDialog.this.updateButtons();
                EnterTextDialog.this.dismiss();
            }
        });
        ((Button) findViewById(R.id.buttonSendWithoutSaving)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.EnterTextDialog.View$OnClickListenerC00074 */

            public void onClick(View v) {
                EnterTextDialog.this.sendText(EnterTextDialog.this._textEnterText.getText().toString());
                EnterTextDialog.this._textEnterText.setText("");
                EnterTextDialog.this._historyIndex = EnterTextDialog.this._history.size();
                EnterTextDialog.this.updateButtons();
                EnterTextDialog.this.dismiss();
            }
        });
        ((ImageButton) findViewById(R.id.buttonTextDelete)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.EnterTextDialog.View$OnClickListenerC00085 */

            public void onClick(View v) {
                if (EnterTextDialog.this._historyIndex < EnterTextDialog.this._history.size()) {
                    String s = EnterTextDialog.this._textEnterText.getText().toString();
                    SentTextBean bean = (SentTextBean) EnterTextDialog.this._history.get(EnterTextDialog.this._historyIndex);
                    if (s.equals(bean.getSentText())) {
                        bean.Gen_delete(EnterTextDialog.this._canvasActivity.database.getWritableDatabase());
                        EnterTextDialog.this._history.remove(EnterTextDialog.this._historyIndex);
                        if (EnterTextDialog.this._historyIndex > 0) {
                            EnterTextDialog.this._historyIndex--;
                        }
                    }
                }
                String s2 = "";
                if (EnterTextDialog.this._historyIndex < EnterTextDialog.this._history.size()) {
                    s2 = ((SentTextBean) EnterTextDialog.this._history.get(EnterTextDialog.this._historyIndex)).getSentText();
                }
                EnterTextDialog.this._textEnterText.setText(s2);
                EnterTextDialog.this.updateButtons();
            }
        });
        Cursor readInOrder = this._canvasActivity.database.getReadableDatabase().rawQuery("select * from SENT_TEXT ORDER BY _id", null);
        try {
            SentTextBean.Gen_populateFromCursor(readInOrder, this._history, SentTextBean.GEN_NEW);
            readInOrder.close();
            this._historyIndex = this._history.size();
            updateButtons();
        } catch (Throwable th) {
            readInOrder.close();
            throw th;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateButtons() {
        boolean z;
        boolean z2 = true;
        ImageButton imageButton = this._buttonPreviousEntry;
        if (this._historyIndex > 0) {
            z = true;
        } else {
            z = false;
        }
        imageButton.setEnabled(z);
        ImageButton imageButton2 = this._buttonNextEntry;
        if (this._historyIndex >= this._history.size()) {
            z2 = false;
        }
        imageButton2.setEnabled(z2);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this._textEnterText.requestFocus();
    }
}
