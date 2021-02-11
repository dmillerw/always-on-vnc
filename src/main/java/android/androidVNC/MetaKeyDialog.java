package android.androidVNC;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

class MetaKeyDialog extends Dialog implements ConnectionSettable {
    static final String[] EMPTY_ARGS = new String[0];
    static ArrayList<MetaList> _lists;
    private static String copyListString;
    VncCanvasActivity _canvasActivity;
    CheckBox _checkAlt;
    CheckBox _checkCtrl;
    CheckBox _checkShift;
    ConnectionBean _connection;
    MetaKeyBean _currentKeyBean;
    VncDatabase _database;
    private boolean _justStarted;
    ArrayList<MetaKeyBean> _keysInList;
    long _listId;
    Spinner _spinnerKeySelect;
    Spinner _spinnerKeysInList;
    Spinner _spinnerLists;
    TextView _textKeyDesc;
    EditText _textListName;

    public MetaKeyDialog(Context context) {
        super(context);
        setOwnerActivity((Activity) context);
        this._canvasActivity = (VncCanvasActivity) context;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this._canvasActivity.getMenuInflater().inflate(R.menu.metakeymenu, menu);
        menu.findItem(R.id.itemDeleteKeyList).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            /* class android.androidVNC.MetaKeyDialog.MenuItem$OnMenuItemClickListenerC00181 */

            public boolean onMenuItemClick(MenuItem item) {
                Utils.showYesNoPrompt(MetaKeyDialog.this._canvasActivity, "Delete key list", "Delete list " + MetaKeyDialog.this._textListName.getText().toString(), new DialogInterface.OnClickListener() {
                    /* class android.androidVNC.MetaKeyDialog.MenuItem$OnMenuItemClickListenerC00181.DialogInterface$OnClickListenerC00191 */

                    public void onClick(DialogInterface dialog, int which) {
                        int position = MetaKeyDialog.this._spinnerLists.getSelectedItemPosition();
                        if (position != -1) {
                            MetaKeyDialog.this._listId = MetaKeyDialog._lists.get(position).get_Id();
                            if (MetaKeyDialog.this._listId > 1) {
                                MetaKeyDialog._lists.remove(position);
                                ArrayAdapter<String> adapter = MetaKeyDialog.getSpinnerAdapter(MetaKeyDialog.this._spinnerLists);
                                adapter.remove(adapter.getItem(position));
                                SQLiteDatabase db = MetaKeyDialog.this._database.getWritableDatabase();
                                db.execSQL(MessageFormat.format("DELETE FROM {0} WHERE {1} = {2}", AbstractMetaKeyBean.GEN_TABLE_NAME, "METALISTID", Long.valueOf(MetaKeyDialog.this._listId)));
                                db.execSQL(MessageFormat.format("DELETE FROM {0} WHERE {1} = {2}", MetaList.GEN_TABLE_NAME, "_id", Long.valueOf(MetaKeyDialog.this._listId)));
                                MetaKeyDialog.this._connection.setMetaListId(1);
                                MetaKeyDialog.this._connection.save(db);
                                MetaKeyDialog.this.setMetaKeyList();
                            }
                        }
                    }
                }, null);
                return true;
            }
        });
        menu.findItem(R.id.itemDeleteKey).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            /* class android.androidVNC.MetaKeyDialog.MenuItem$OnMenuItemClickListenerC00202 */

            public boolean onMenuItemClick(MenuItem item) {
                final int position = MetaKeyDialog.this._spinnerKeysInList.getSelectedItemPosition();
                if (position == -1) {
                    return true;
                }
                final MetaKeyBean toDelete = MetaKeyDialog.this._keysInList.get(position);
                Utils.showYesNoPrompt(MetaKeyDialog.this._canvasActivity, "Delete from list", "Delete key " + toDelete.getKeyDesc(), new DialogInterface.OnClickListener() {
                    /* class android.androidVNC.MetaKeyDialog.MenuItem$OnMenuItemClickListenerC00202.DialogInterface$OnClickListenerC00211 */

                    public void onClick(DialogInterface dialog, int which) {
                        MetaKeyDialog.getSpinnerAdapter(MetaKeyDialog.this._spinnerKeysInList).remove(toDelete.getKeyDesc());
                        MetaKeyDialog.this._keysInList.remove(position);
                        SQLiteDatabase db = MetaKeyDialog.this._database.getWritableDatabase();
                        db.execSQL(MessageFormat.format("DELETE FROM {0} WHERE {1} = {2}", AbstractMetaKeyBean.GEN_TABLE_NAME, "METALISTID", Long.valueOf(toDelete.get_Id())));
                        if (MetaKeyDialog.this._connection.getLastMetaKeyId() == toDelete.get_Id()) {
                            MetaKeyDialog.this._connection.setLastMetaKeyId(0);
                            MetaKeyDialog.this._connection.save(db);
                        }
                        int newPos = MetaKeyDialog.this._spinnerKeysInList.getSelectedItemPosition();
                        if (newPos != -1 && newPos < MetaKeyDialog.this._keysInList.size()) {
                            MetaKeyDialog.this._currentKeyBean = new MetaKeyBean(MetaKeyDialog.this._keysInList.get(newPos));
                            MetaKeyDialog.this.updateDialogForCurrentKey();
                        }
                    }
                }, null);
                return true;
            }
        });
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean z;
        boolean z2 = false;
        MenuItem findItem = menu.findItem(R.id.itemDeleteKeyList);
        if (this._currentKeyBean.getMetaListId() > 1) {
            z = true;
        } else {
            z = false;
        }
        findItem.setEnabled(z);
        MenuItem findItem2 = menu.findItem(R.id.itemDeleteKey);
        if (this._spinnerKeysInList.getSelectedItemPosition() != -1) {
            z2 = true;
        }
        findItem2.setEnabled(z2);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metakey);
        setTitle(R.string.meta_key_title);
        this._checkShift = (CheckBox) findViewById(R.id.checkboxShift);
        this._checkCtrl = (CheckBox) findViewById(R.id.checkboxCtrl);
        this._checkAlt = (CheckBox) findViewById(R.id.checkboxAlt);
        this._textKeyDesc = (TextView) findViewById(R.id.textKeyDesc);
        this._textListName = (EditText) findViewById(R.id.textListName);
        this._spinnerKeySelect = (Spinner) findViewById(R.id.spinnerKeySelect);
        this._spinnerKeysInList = (Spinner) findViewById(R.id.spinnerKeysInList);
        this._spinnerLists = (Spinner) findViewById(R.id.spinnerLists);
        this._database = this._canvasActivity.database;
        if (_lists == null) {
            _lists = new ArrayList<>();
            MetaList.getAll(this._database.getReadableDatabase(), MetaList.GEN_TABLE_NAME, _lists, MetaList.GEN_NEW);
        }
        this._spinnerKeySelect.setAdapter((SpinnerAdapter) new ArrayAdapter(getOwnerActivity(), 17367048, MetaKeyBean.allKeysNames));
        this._spinnerKeySelect.setSelection(0);
        setListSpinner();
        this._checkShift.setOnCheckedChangeListener(new MetaCheckListener(1));
        this._checkAlt.setOnCheckedChangeListener(new MetaCheckListener(2));
        this._checkCtrl.setOnCheckedChangeListener(new MetaCheckListener(4));
        this._spinnerLists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class android.androidVNC.MetaKeyDialog.C00223 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                MetaKeyDialog.this._connection.setMetaListId(MetaKeyDialog._lists.get(position).get_Id());
                MetaKeyDialog.this._connection.Gen_update(MetaKeyDialog.this._database.getWritableDatabase());
                MetaKeyDialog.this.setMetaKeyList();
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this._spinnerKeysInList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class android.androidVNC.MetaKeyDialog.C00234 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                MetaKeyDialog.this._currentKeyBean = new MetaKeyBean(MetaKeyDialog.this._keysInList.get(position));
                MetaKeyDialog.this.updateDialogForCurrentKey();
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this._spinnerKeySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class android.androidVNC.MetaKeyDialog.C00245 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (MetaKeyDialog.this._currentKeyBean == null) {
                    MetaKeyDialog.this._currentKeyBean = new MetaKeyBean(0, 0, MetaKeyBean.allKeys.get(position));
                } else {
                    MetaKeyDialog.this._currentKeyBean.setKeyBase(MetaKeyBean.allKeys.get(position));
                }
                MetaKeyDialog.this.updateDialogForCurrentKey();
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ((Button) findViewById(R.id.buttonSend)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.MetaKeyDialog.View$OnClickListenerC00256 */

            public void onClick(View v) {
                MetaKeyDialog.this.sendCurrentKey();
                MetaKeyDialog.this.dismiss();
            }
        });
        ((Button) findViewById(R.id.buttonNewList)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.MetaKeyDialog.View$OnClickListenerC00267 */

            public void onClick(View v) {
                MetaList newList = new MetaList();
                newList.setName("New");
                SQLiteDatabase db = MetaKeyDialog.this._database.getWritableDatabase();
                newList.Gen_insert(db);
                MetaKeyDialog.this._connection.setMetaListId(newList.get_Id());
                MetaKeyDialog.this._connection.save(db);
                MetaKeyDialog._lists.add(newList);
                MetaKeyDialog.getSpinnerAdapter(MetaKeyDialog.this._spinnerLists).add(newList.getName());
                MetaKeyDialog.this.setMetaKeyList();
            }
        });
        ((Button) findViewById(R.id.buttonCopyList)).setOnClickListener(new View.OnClickListener() {
            /* class android.androidVNC.MetaKeyDialog.View$OnClickListenerC00278 */

            public void onClick(View v) {
                MetaList newList = new MetaList();
                newList.setName("Copy of " + MetaKeyDialog.this._textListName.getText().toString());
                SQLiteDatabase db = MetaKeyDialog.this._database.getWritableDatabase();
                newList.Gen_insert(db);
                db.execSQL(MessageFormat.format(MetaKeyDialog.this.getCopyListString(), Long.valueOf(newList.get_Id()), Long.valueOf(MetaKeyDialog.this._listId)));
                MetaKeyDialog.this._connection.setMetaListId(newList.get_Id());
                MetaKeyDialog.this._connection.save(db);
                MetaKeyDialog._lists.add(newList);
                MetaKeyDialog.getSpinnerAdapter(MetaKeyDialog.this._spinnerLists).add(newList.getName());
                MetaKeyDialog.this.setMetaKeyList();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getCopyListString() {
        if (copyListString == null) {
            StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(AbstractMetaKeyBean.GEN_TABLE_NAME);
            sb.append(" ( ");
            sb.append("METALISTID");
            StringBuilder fieldList = new StringBuilder();
            for (Map.Entry<String, Object> s : this._currentKeyBean.Gen_getValues().valueSet()) {
                if (!s.getKey().equals("_id") && !s.getKey().equals("METALISTID")) {
                    fieldList.append(',');
                    fieldList.append(s.getKey());
                }
            }
            String fl = fieldList.toString();
            sb.append(fl);
            sb.append(" ) SELECT {0} ");
            sb.append(fl);
            sb.append(" FROM ");
            sb.append(AbstractMetaKeyBean.GEN_TABLE_NAME);
            sb.append(" WHERE ");
            sb.append("METALISTID");
            sb.append(" = {1}");
            copyListString = sb.toString();
        }
        return copyListString;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        takeKeyEvents(true);
        this._justStarted = true;
        super.onStart();
        View v = getCurrentFocus();
        if (v != null) {
            v.clearFocus();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        int i = 0;
        Iterator<MetaList> it = _lists.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MetaList l = it.next();
            if (l.get_Id() == this._listId) {
                String s = this._textListName.getText().toString();
                if (!s.equals(l.getName())) {
                    l.setName(s);
                    l.Gen_update(this._database.getWritableDatabase());
                    ArrayAdapter<String> adapter = getSpinnerAdapter(this._spinnerLists);
                    adapter.remove(adapter.getItem(i));
                    adapter.insert(s, i);
                }
            } else {
                i++;
            }
        }
        takeKeyEvents(false);
        super.onStop();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this._justStarted = false;
        if (keyCode == 4 || keyCode == 82 || getCurrentFocus() != null) {
            return super.onKeyDown(keyCode, event);
        }
        int flags = event.getMetaState();
        int currentFlags = this._currentKeyBean.getMetaFlags();
        MetaKeyBase base = MetaKeyBean.keysByKeyCode.get(Integer.valueOf(keyCode));
        if (base != null) {
            if ((flags & 1) != 0) {
                currentFlags |= 1;
            }
            if ((flags & 2) != 0) {
                currentFlags |= 2;
            }
            this._currentKeyBean.setKeyBase(base);
        } else {
            if ((flags & 1) != 0) {
                currentFlags ^= 1;
            }
            if ((flags & 2) != 0) {
                currentFlags ^= 2;
            }
            if (keyCode == 84) {
                currentFlags ^= 4;
            }
        }
        this._currentKeyBean.setMetaFlags(currentFlags);
        updateDialogForCurrentKey();
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this._justStarted || keyCode == 4 || keyCode == 82 || getCurrentFocus() != null) {
            this._justStarted = false;
            return super.onKeyUp(keyCode, event);
        }
        if (MetaKeyBean.keysByKeyCode.get(Integer.valueOf(keyCode)) != null) {
            sendCurrentKey();
            dismiss();
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static ArrayAdapter<String> getSpinnerAdapter(Spinner spinner) {
        return (ArrayAdapter) spinner.getAdapter();
    }

    /* access modifiers changed from: package-private */
    public void sendCurrentKey() {
        int index = Collections.binarySearch(this._keysInList, this._currentKeyBean);
        SQLiteDatabase db = this._database.getWritableDatabase();
        if (index < 0) {
            int insertionPoint = -(index + 1);
            this._currentKeyBean.Gen_insert(db);
            this._keysInList.add(insertionPoint, this._currentKeyBean);
            getSpinnerAdapter(this._spinnerKeysInList).insert(this._currentKeyBean.getKeyDesc(), insertionPoint);
            this._spinnerKeysInList.setSelection(insertionPoint);
            this._connection.setLastMetaKeyId(this._currentKeyBean.get_Id());
        } else {
            this._connection.setLastMetaKeyId(this._keysInList.get(index).get_Id());
            this._spinnerKeysInList.setSelection(index);
        }
        this._connection.Gen_update(db);
        this._canvasActivity.vncCanvas.sendMetaKey(this._currentKeyBean);
    }

    /* access modifiers changed from: package-private */
    public void setMetaKeyList() {
        long listId = this._connection.getMetaListId();
        if (listId != this._listId) {
            int i = 0;
            while (true) {
                if (i >= _lists.size()) {
                    break;
                }
                MetaList list = _lists.get(i);
                if (list.get_Id() == listId) {
                    this._spinnerLists.setSelection(i);
                    this._keysInList = new ArrayList<>();
                    Cursor c = this._database.getReadableDatabase().rawQuery(MessageFormat.format("SELECT * FROM {0} WHERE {1} = {2} ORDER BY KEYDESC", AbstractMetaKeyBean.GEN_TABLE_NAME, "METALISTID", Long.valueOf(listId)), EMPTY_ARGS);
                    MetaKeyBean.Gen_populateFromCursor(c, this._keysInList, MetaKeyBean.NEW);
                    c.close();
                    ArrayList<String> keys = new ArrayList<>(this._keysInList.size());
                    int selectedOffset = 0;
                    long lastSelectedKeyId = this._canvasActivity.getConnection().getLastMetaKeyId();
                    for (int j = 0; j < this._keysInList.size(); j++) {
                        MetaKeyBean key = this._keysInList.get(j);
                        keys.add(key.getKeyDesc());
                        if (lastSelectedKeyId == key.get_Id()) {
                            selectedOffset = j;
                        }
                    }
                    this._spinnerKeysInList.setAdapter((SpinnerAdapter) new ArrayAdapter(getOwnerActivity(), 17367048, keys));
                    if (keys.size() > 0) {
                        this._spinnerKeysInList.setSelection(selectedOffset);
                        this._currentKeyBean = new MetaKeyBean(this._keysInList.get(selectedOffset));
                    } else {
                        this._currentKeyBean = new MetaKeyBean(listId, 0, MetaKeyBean.allKeys.get(0));
                    }
                    updateDialogForCurrentKey();
                    this._textListName.setText(list.getName());
                } else {
                    i++;
                }
            }
            this._listId = listId;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateDialogForCurrentKey() {
        boolean z;
        boolean z2;
        MetaKeyBase base;
        int index;
        boolean z3 = true;
        int flags = this._currentKeyBean.getMetaFlags();
        CheckBox checkBox = this._checkAlt;
        if ((flags & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setChecked(z);
        CheckBox checkBox2 = this._checkShift;
        if ((flags & 1) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        checkBox2.setChecked(z2);
        CheckBox checkBox3 = this._checkCtrl;
        if ((flags & 4) == 0) {
            z3 = false;
        }
        checkBox3.setChecked(z3);
        if (this._currentKeyBean.isMouseClick()) {
            base = MetaKeyBean.keysByMouseButton.get(Integer.valueOf(this._currentKeyBean.getMouseButtons()));
        } else {
            base = MetaKeyBean.keysByKeySym.get(Integer.valueOf(this._currentKeyBean.getKeySym()));
        }
        if (base != null && (index = Collections.binarySearch(MetaKeyBean.allKeys, base)) >= 0) {
            this._spinnerKeySelect.setSelection(index);
        }
        this._textKeyDesc.setText(this._currentKeyBean.getKeyDesc());
    }

    @Override // android.androidVNC.ConnectionSettable
    public void setConnection(ConnectionBean conn) {
        if (this._connection != conn) {
            this._connection = conn;
            setMetaKeyList();
        }
    }

    /* access modifiers changed from: package-private */
    public void setListSpinner() {
        ArrayList<String> listNames = new ArrayList<>(_lists.size());
        for (int i = 0; i < _lists.size(); i++) {
            listNames.add(_lists.get(i).getName());
        }
        this._spinnerLists.setAdapter((SpinnerAdapter) new ArrayAdapter(getOwnerActivity(), 17367048, listNames));
    }

    class MetaCheckListener implements CompoundButton.OnCheckedChangeListener {
        private int _mask;

        MetaCheckListener(int mask) {
            this._mask = mask;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                MetaKeyDialog.this._currentKeyBean.setMetaFlags(MetaKeyDialog.this._currentKeyBean.getMetaFlags() | this._mask);
            } else {
                MetaKeyDialog.this._currentKeyBean.setMetaFlags(MetaKeyDialog.this._currentKeyBean.getMetaFlags() & (this._mask ^ -1));
            }
            MetaKeyDialog.this._textKeyDesc.setText(MetaKeyDialog.this._currentKeyBean.getKeyDesc());
        }
    }
}
