package android.androidVNC;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

/* renamed from: android.androidVNC.androidVNC */
public class AndroidVNC extends Activity {
    private CheckBox checkboxKeepPassword;
    private CheckBox checkboxLocalCursor;
    private CheckBox checkboxUseImmersive;
    private CheckBox checkboxWakeLock;
    private Spinner colorSpinner;
    private VncDatabase database;
    private Button goButton;
    private RadioGroup groupForceFullScreen;
    private EditText ipText;
    private EditText passwordText;
    private EditText portText;
    private TextView repeaterText;
    private boolean repeaterTextSet;
    private ConnectionBean selected;
    private Spinner spinnerConnection;
    private EditText textNickname;
    private EditText textUsername;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        this.ipText = (EditText) findViewById(R.id.textIP);
        this.portText = (EditText) findViewById(R.id.textPORT);
        this.passwordText = (EditText) findViewById(R.id.textPASSWORD);
        this.textNickname = (EditText) findViewById(R.id.textNickname);
        this.textUsername = (EditText) findViewById(R.id.textUsername);
        this.goButton = (Button) findViewById(R.id.buttonGO);
        ((Button) findViewById(R.id.buttonRepeater)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.ActivityC0051androidVNC.View$OnClickListenerC00521 */

            public void onClick(View v) {
                AndroidVNC.this.showDialog(R.layout.repeater_dialog);
            }
        });
        ((Button) findViewById(R.id.buttonImportExport)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.ActivityC0051androidVNC.View$OnClickListenerC00532 */

            public void onClick(View v) {
                AndroidVNC.this.showDialog(R.layout.importexport);
            }
        });
        this.colorSpinner = (Spinner) findViewById(R.id.colorformat);
        ArrayAdapter<COLORMODEL> colorSpinnerAdapter = new ArrayAdapter<>(this, 17367048, COLORMODEL.values());
        this.groupForceFullScreen = (RadioGroup) findViewById(R.id.groupForceFullScreen);
        this.checkboxKeepPassword = (CheckBox) findViewById(R.id.checkboxKeepPassword);
        this.checkboxLocalCursor = (CheckBox) findViewById(R.id.checkboxUseLocalCursor);
        this.checkboxUseImmersive = (CheckBox) findViewById(R.id.checkUseImmersive);
        this.checkboxWakeLock = (CheckBox) findViewById(R.id.checkUseWakeLock);
        this.colorSpinner.setAdapter((SpinnerAdapter) colorSpinnerAdapter);
        this.colorSpinner.setSelection(0);
        this.spinnerConnection = (Spinner) findViewById(R.id.spinnerConnection);
        this.spinnerConnection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class android.androidVNC.ActivityC0051androidVNC.C00543 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> ad, View view, int itemIndex, long id) {
                AndroidVNC.this.selected = (ConnectionBean) ad.getSelectedItem();
                AndroidVNC.this.updateViewFromSelected();
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
                AndroidVNC.this.selected = null;
            }
        });
        this.spinnerConnection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /* class android.androidVNC.ActivityC0051androidVNC.C00554 */

            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                AndroidVNC.this.spinnerConnection.setSelection(arg2);
                AndroidVNC.this.selected = (ConnectionBean) AndroidVNC.this.spinnerConnection.getItemAtPosition(arg2);
                AndroidVNC.this.canvasStart();
                return true;
            }
        });
        this.repeaterText = (TextView) findViewById(R.id.textRepeaterId);
        this.goButton.setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.ActivityC0051androidVNC.View$OnClickListenerC00565 */

            public void onClick(View view) {
                AndroidVNC.this.canvasStart();
            }
        });
        this.database = new VncDatabase(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.database.close();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        if (id == R.layout.importexport) {
            return new ImportExportDialog(this);
        }
        return new RepeaterDialog(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.androidvncmenu, menu);
        return true;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        boolean z;
        boolean z2 = false;
        MenuItem findItem = menu.findItem(R.id.itemDeleteConnection);
        if (this.selected == null || this.selected.isNew()) {
            z = false;
        } else {
            z = true;
        }
        findItem.setEnabled(z);
        MenuItem findItem2 = menu.findItem(R.id.itemSaveAsCopy);
        if (this.selected != null && !this.selected.isNew()) {
            z2 = true;
        }
        findItem2.setEnabled(z2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSaveAsCopy:
                if (this.selected.getNickname().equals(this.textNickname.getText().toString())) {
                    this.textNickname.setText("Copy of " + this.selected.getNickname());
                }
                updateSelectedFromView();
                this.selected.set_Id(0);
                saveAndWriteRecent();
                arriveOnPage();
                return true;
            case R.id.itemDeleteConnection:
                Utils.showYesNoPrompt(this, "Delete?", "Delete " + this.selected.getNickname() + "?", new DialogInterface.OnClickListener() {
                    /* class android.androidVNC.ActivityC0051androidVNC.DialogInterface$OnClickListenerC00576 */

                    public void onClick(DialogInterface dialog, int i) {
                        AndroidVNC.this.selected.Gen_delete(AndroidVNC.this.database.getWritableDatabase());
                        AndroidVNC.this.arriveOnPage();
                    }
                }, null);
                return true;
            case R.id.itemOpenDoc:
                Utils.showDocumentation(this);
                return true;
            default:
                return true;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateViewFromSelected() {
        if (this.selected != null) {
            this.ipText.setText(this.selected.getAddress());
            this.portText.setText(Integer.toString(this.selected.getPort()));
            if (this.selected.getKeepPassword() || this.selected.getPassword().length() > 0) {
                this.passwordText.setText(this.selected.getPassword());
            }
            this.groupForceFullScreen.check(this.selected.getForceFull() == 0 ? R.id.radioForceFullScreenAuto : this.selected.getForceFull() == 1 ? R.id.radioForceFullScreenOn : R.id.radioForceFullScreenOff);
            this.checkboxKeepPassword.setChecked(this.selected.getKeepPassword());
            this.checkboxLocalCursor.setChecked(this.selected.getUseLocalCursor());
            this.checkboxWakeLock.setChecked(this.selected.getUseWakeLock());
            this.checkboxUseImmersive.setChecked(this.selected.getUseImmersive());
            this.textNickname.setText(this.selected.getNickname());
            this.textUsername.setText(this.selected.getUserName());
            COLORMODEL cm = COLORMODEL.valueOf(this.selected.getColorModel());
            COLORMODEL[] colors = COLORMODEL.values();
            int i = 0;
            while (true) {
                if (i >= colors.length) {
                    break;
                } else if (colors[i] == cm) {
                    this.colorSpinner.setSelection(i);
                    break;
                } else {
                    i++;
                }
            }
            updateRepeaterInfo(this.selected.getUseRepeater(), this.selected.getRepeaterId());
        }
    }

    /* access modifiers changed from: package-private */
    public void updateRepeaterInfo(boolean useRepeater, String repeaterId) {
        if (useRepeater) {
            this.repeaterText.setText(repeaterId);
            this.repeaterTextSet = true;
            return;
        }
        this.repeaterText.setText(getText(R.string.repeater_empty_text));
        this.repeaterTextSet = false;
    }

    private void updateSelectedFromView() {
        long j;
        if (this.selected != null) {
            this.selected.setAddress(this.ipText.getText().toString());
            try {
                this.selected.setPort(Integer.parseInt(this.portText.getText().toString()));
            } catch (NumberFormatException e) {
            }
            this.selected.setNickname(this.textNickname.getText().toString());
            this.selected.setUserName(this.textUsername.getText().toString());
            ConnectionBean connectionBean = this.selected;
            if (this.groupForceFullScreen.getCheckedRadioButtonId() == R.id.radioForceFullScreenAuto) {
                j = 0;
            } else {
                j = this.groupForceFullScreen.getCheckedRadioButtonId() == R.id.radioForceFullScreenOn ? 1 : 2;
            }
            connectionBean.setForceFull(j);
            this.selected.setPassword(this.passwordText.getText().toString());
            this.selected.setKeepPassword(this.checkboxKeepPassword.isChecked());
            this.selected.setUseLocalCursor(this.checkboxLocalCursor.isChecked());
            this.selected.setColorModel(((COLORMODEL) this.colorSpinner.getSelectedItem()).nameString());
            this.selected.setUseWakeLock(this.checkboxWakeLock.isChecked());
            this.selected.setUseImmersive(this.checkboxUseImmersive.isChecked());
            if (this.repeaterTextSet) {
                this.selected.setRepeaterId(this.repeaterText.getText().toString());
                this.selected.setUseRepeater(true);
                return;
            }
            this.selected.setUseRepeater(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        arriveOnPage();
    }

    static MostRecentBean getMostRecent(SQLiteDatabase db) {
        ArrayList<MostRecentBean> recents = new ArrayList<>(1);
        MostRecentBean.getAll(db, MostRecentBean.GEN_TABLE_NAME, recents, MostRecentBean.GEN_NEW);
        if (recents.size() == 0) {
            return null;
        }
        return recents.get(0);
    }

    /* access modifiers changed from: package-private */
    public void arriveOnPage() {
        MostRecentBean mostRecent;
        ArrayList<ConnectionBean> connections = new ArrayList<>();
        ConnectionBean.getAll(this.database.getReadableDatabase(), AbstractConnectionBean.GEN_TABLE_NAME, connections, ConnectionBean.newInstance);
        Collections.sort(connections);
        connections.add(0, new ConnectionBean());
        int connectionIndex = 0;
        if (connections.size() > 1 && (mostRecent = getMostRecent(this.database.getReadableDatabase())) != null) {
            int i = 1;
            while (true) {
                if (i >= connections.size()) {
                    break;
                } else if (connections.get(i).get_Id() == mostRecent.getConnectionId()) {
                    connectionIndex = i;
                    break;
                } else {
                    i++;
                }
            }
        }
        this.spinnerConnection.setAdapter((SpinnerAdapter) new ArrayAdapter(this, 17367048, connections.toArray(new ConnectionBean[connections.size()])));
        this.spinnerConnection.setSelection(connectionIndex, false);
        this.selected = connections.get(connectionIndex);
        updateViewFromSelected();
        IntroTextDialog.showIntroTextIfNecessary(this, this.database);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.selected != null) {
            updateSelectedFromView();
            this.selected.save(this.database.getWritableDatabase());
        }
    }

    /* access modifiers changed from: package-private */
    public VncDatabase getDatabaseHelper() {
        return this.database;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void canvasStart() {
        if (this.selected != null) {
            if (Utils.getMemoryInfo(this).lowMemory) {
                Utils.showYesNoPrompt(this, "Continue?", "Android reports low system memory.\nContinue with VNC connection?", new DialogInterface.OnClickListener() {
                    /* class android.androidVNC.ActivityC0051androidVNC.DialogInterface$OnClickListenerC00587 */

                    public void onClick(DialogInterface dialog, int which) {
                        AndroidVNC.this.vnc();
                    }
                }, null);
            } else {
                vnc();
            }
        }
    }

    private void saveAndWriteRecent() {
        SQLiteDatabase db = this.database.getWritableDatabase();
        db.beginTransaction();
        try {
            this.selected.save(db);
            MostRecentBean mostRecent = getMostRecent(db);
            if (mostRecent == null) {
                MostRecentBean mostRecent2 = new MostRecentBean();
                mostRecent2.setConnectionId(this.selected.get_Id());
                mostRecent2.Gen_insert(db);
            } else {
                mostRecent.setConnectionId(this.selected.get_Id());
                mostRecent.Gen_update(db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void vnc() {
        updateSelectedFromView();
        saveAndWriteRecent();
        Intent intent = new Intent(this, VncCanvasActivity.class);
        intent.putExtra(VncConstants.CONNECTION, this.selected.Gen_getValues());
        startActivity(intent);
    }
}
