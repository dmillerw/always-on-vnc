package android.androidVNC;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.antlersoft.android.contentxml.SqliteElement;
import com.antlersoft.android.bc.BCFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.xml.sax.SAXException;

class ImportExportDialog extends Dialog {
    private AndroidVNC _configurationDialog;
    private EditText _textLoadUrl;
    private EditText _textSaveUrl;

    public ImportExportDialog(AndroidVNC context) {
        super(context);
        setOwnerActivity(context);
        this._configurationDialog = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexport);
        setTitle(R.string.import_export_settings);
        this._textLoadUrl = (EditText) findViewById(R.id.textImportUrl);
        this._textSaveUrl = (EditText) findViewById(R.id.textExportPath);
        File f = BCFactory.getInstance().getStorageContext().getExternalStorageDir(this._configurationDialog, null);
        if (f != null) {
            File f2 = new File(f, "vnc_settings.xml");
            this._textSaveUrl.setText(f2.getAbsolutePath());
            try {
                this._textLoadUrl.setText(f2.toURL().toString());
            } catch (MalformedURLException e) {
            }
            ((Button) findViewById(R.id.buttonExport)).setOnClickListener(new View.OnClickListener() {
                /* class android.androidVNC.ImportExportDialog.View$OnClickListenerC00091 */

                public void onClick(View v) {
                    try {
                        Writer writer = new OutputStreamWriter(new FileOutputStream(new File(ImportExportDialog.this._textSaveUrl.getText().toString()), false));
                        SqliteElement.exportDbAsXmlToStream(ImportExportDialog.this._configurationDialog.getDatabaseHelper().getReadableDatabase(), writer);
                        writer.close();
                        ImportExportDialog.this.dismiss();
                    } catch (IOException ioe) {
                        ImportExportDialog.this.errorNotify("I/O Exception exporting config", ioe);
                    } catch (SAXException e) {
                        ImportExportDialog.this.errorNotify("XML Exception exporting config", e);
                    }
                }
            });
            ((Button) findViewById(R.id.buttonImport)).setOnClickListener(new View.OnClickListener() {
                /* class android.androidVNC.ImportExportDialog.View$OnClickListenerC00102 */

                public void onClick(View v) {
                    try {
                        URLConnection connection = new URL(ImportExportDialog.this._textLoadUrl.getText().toString()).openConnection();
                        connection.connect();
                        SqliteElement.importXmlStreamToDb(ImportExportDialog.this._configurationDialog.getDatabaseHelper().getWritableDatabase(), new InputStreamReader(connection.getInputStream()), SqliteElement.ReplaceStrategy.REPLACE_EXISTING);
                        ImportExportDialog.this.dismiss();
                        ImportExportDialog.this._configurationDialog.arriveOnPage();
                    } catch (MalformedURLException mfe) {
                        ImportExportDialog.this.errorNotify("Improper URL given: " + ((Object) ImportExportDialog.this._textLoadUrl.getText()), mfe);
                    } catch (IOException ioe) {
                        ImportExportDialog.this.errorNotify("I/O error reading configuration", ioe);
                    } catch (SAXException e) {
                        ImportExportDialog.this.errorNotify("XML or format error reading configuration", e);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void errorNotify(String msg, Throwable t) {
        Log.i("android.androidVNC.ImportExportDialog", msg, t);
        Utils.showErrorMessage(getContext(), msg + ":" + t.getMessage());
    }
}
