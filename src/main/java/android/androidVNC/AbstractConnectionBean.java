package android.androidVNC;

import android.content.ContentValues;
import android.database.Cursor;
import com.antlersoft.android.dbimpl.IdImplementationBase;

public abstract class AbstractConnectionBean extends IdImplementationBase implements IConnectionBean {
    public static final int GEN_COUNT = 23;
    public static String GEN_CREATE = "CREATE TABLE CONNECTION_BEAN (_id INTEGER PRIMARY KEY AUTOINCREMENT,NICKNAME TEXT,ADDRESS TEXT,PORT INTEGER,PASSWORD TEXT,COLORMODEL TEXT,FORCEFULL INTEGER,REPEATERID TEXT,INPUTMODE TEXT,SCALEMODE TEXT,USELOCALCURSOR INTEGER,KEEPPASSWORD INTEGER,FOLLOWMOUSE INTEGER,USEREPEATER INTEGER,METALISTID INTEGER,LAST_META_KEY_ID INTEGER,FOLLOWPAN INTEGER DEFAULT 0,USERNAME TEXT,SECURECONNECTIONTYPE TEXT,SHOWZOOMBUTTONS INTEGER DEFAULT 1,DOUBLE_TAP_ACTION TEXT,USEIMMERSIVE INTEGER DEFAULT 1,USEWAKELOCK INTEGER DEFAULT 0)";
    public static final String GEN_FIELD_ADDRESS = "ADDRESS";
    public static final String GEN_FIELD_COLORMODEL = "COLORMODEL";
    public static final String GEN_FIELD_DOUBLE_TAP_ACTION = "DOUBLE_TAP_ACTION";
    public static final String GEN_FIELD_FOLLOWMOUSE = "FOLLOWMOUSE";
    public static final String GEN_FIELD_FOLLOWPAN = "FOLLOWPAN";
    public static final String GEN_FIELD_FORCEFULL = "FORCEFULL";
    public static final String GEN_FIELD_INPUTMODE = "INPUTMODE";
    public static final String GEN_FIELD_KEEPPASSWORD = "KEEPPASSWORD";
    public static final String GEN_FIELD_LAST_META_KEY_ID = "LAST_META_KEY_ID";
    public static final String GEN_FIELD_METALISTID = "METALISTID";
    public static final String GEN_FIELD_NICKNAME = "NICKNAME";
    public static final String GEN_FIELD_PASSWORD = "PASSWORD";
    public static final String GEN_FIELD_PORT = "PORT";
    public static final String GEN_FIELD_REPEATERID = "REPEATERID";
    public static final String GEN_FIELD_SCALEMODE = "SCALEMODE";
    public static final String GEN_FIELD_SECURECONNECTIONTYPE = "SECURECONNECTIONTYPE";
    public static final String GEN_FIELD_SHOWZOOMBUTTONS = "SHOWZOOMBUTTONS";
    public static final String GEN_FIELD_USEIMMERSIVE = "USEIMMERSIVE";
    public static final String GEN_FIELD_USELOCALCURSOR = "USELOCALCURSOR";
    public static final String GEN_FIELD_USEREPEATER = "USEREPEATER";
    public static final String GEN_FIELD_USERNAME = "USERNAME";
    public static final String GEN_FIELD_USEWAKELOCK = "USEWAKELOCK";
    public static final String GEN_FIELD__ID = "_id";
    public static final int GEN_ID_ADDRESS = 2;
    public static final int GEN_ID_COLORMODEL = 5;
    public static final int GEN_ID_DOUBLE_TAP_ACTION = 20;
    public static final int GEN_ID_FOLLOWMOUSE = 12;
    public static final int GEN_ID_FOLLOWPAN = 16;
    public static final int GEN_ID_FORCEFULL = 6;
    public static final int GEN_ID_INPUTMODE = 8;
    public static final int GEN_ID_KEEPPASSWORD = 11;
    public static final int GEN_ID_LAST_META_KEY_ID = 15;
    public static final int GEN_ID_METALISTID = 14;
    public static final int GEN_ID_NICKNAME = 1;
    public static final int GEN_ID_PASSWORD = 4;
    public static final int GEN_ID_PORT = 3;
    public static final int GEN_ID_REPEATERID = 7;
    public static final int GEN_ID_SCALEMODE = 9;
    public static final int GEN_ID_SECURECONNECTIONTYPE = 18;
    public static final int GEN_ID_SHOWZOOMBUTTONS = 19;
    public static final int GEN_ID_USEIMMERSIVE = 21;
    public static final int GEN_ID_USELOCALCURSOR = 10;
    public static final int GEN_ID_USEREPEATER = 13;
    public static final int GEN_ID_USERNAME = 17;
    public static final int GEN_ID_USEWAKELOCK = 22;
    public static final int GEN_ID__ID = 0;
    public static final String GEN_TABLE_NAME = "CONNECTION_BEAN";
    private String gen_DOUBLE_TAP_ACTION;
    private long gen_LAST_META_KEY_ID;
    private String gen_SCALEMODE;
    private long gen__Id;
    private String gen_address;
    private String gen_colorModel;
    private boolean gen_followMouse;
    private boolean gen_followPan;
    private long gen_forceFull;
    private String gen_inputMode;
    private boolean gen_keepPassword;
    private long gen_metaListId;
    private String gen_nickname;
    private String gen_password;
    private int gen_port;
    private String gen_repeaterId;
    private String gen_secureConnectionType;
    private boolean gen_showZoomButtons;
    private boolean gen_useImmersive;
    private boolean gen_useLocalCursor;
    private boolean gen_useRepeater;
    private boolean gen_useWakeLock;
    private String gen_userName;

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public String Gen_tableName() {
        return GEN_TABLE_NAME;
    }

    @Override // android.androidVNC.IConnectionBean, com.antlersoft.android.dbimpl.IdImplementationBase
    public long get_Id() {
        return this.gen__Id;
    }

    @Override // com.antlersoft.android.dbimpl.IdImplementationBase
    public void set_Id(long arg__Id) {
        this.gen__Id = arg__Id;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getNickname() {
        return this.gen_nickname;
    }

    public void setNickname(String arg_nickname) {
        this.gen_nickname = arg_nickname;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getAddress() {
        return this.gen_address;
    }

    public void setAddress(String arg_address) {
        this.gen_address = arg_address;
    }

    @Override // android.androidVNC.IConnectionBean
    public int getPort() {
        return this.gen_port;
    }

    public void setPort(int arg_port) {
        this.gen_port = arg_port;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getPassword() {
        return this.gen_password;
    }

    public void setPassword(String arg_password) {
        this.gen_password = arg_password;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getColorModel() {
        return this.gen_colorModel;
    }

    public void setColorModel(String arg_colorModel) {
        this.gen_colorModel = arg_colorModel;
    }

    @Override // android.androidVNC.IConnectionBean
    public long getForceFull() {
        return this.gen_forceFull;
    }

    public void setForceFull(long arg_forceFull) {
        this.gen_forceFull = arg_forceFull;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getRepeaterId() {
        return this.gen_repeaterId;
    }

    public void setRepeaterId(String arg_repeaterId) {
        this.gen_repeaterId = arg_repeaterId;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getInputMode() {
        return this.gen_inputMode;
    }

    public void setInputMode(String arg_inputMode) {
        this.gen_inputMode = arg_inputMode;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getScaleModeAsString() {
        return this.gen_SCALEMODE;
    }

    public void setScaleModeAsString(String arg_SCALEMODE) {
        this.gen_SCALEMODE = arg_SCALEMODE;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getUseLocalCursor() {
        return this.gen_useLocalCursor;
    }

    public void setUseLocalCursor(boolean arg_useLocalCursor) {
        this.gen_useLocalCursor = arg_useLocalCursor;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getKeepPassword() {
        return this.gen_keepPassword;
    }

    public void setKeepPassword(boolean arg_keepPassword) {
        this.gen_keepPassword = arg_keepPassword;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getFollowMouse() {
        return this.gen_followMouse;
    }

    public void setFollowMouse(boolean arg_followMouse) {
        this.gen_followMouse = arg_followMouse;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getUseRepeater() {
        return this.gen_useRepeater;
    }

    public void setUseRepeater(boolean arg_useRepeater) {
        this.gen_useRepeater = arg_useRepeater;
    }

    @Override // android.androidVNC.IConnectionBean
    public long getMetaListId() {
        return this.gen_metaListId;
    }

    public void setMetaListId(long arg_metaListId) {
        this.gen_metaListId = arg_metaListId;
    }

    @Override // android.androidVNC.IConnectionBean
    public long getLastMetaKeyId() {
        return this.gen_LAST_META_KEY_ID;
    }

    public void setLastMetaKeyId(long arg_LAST_META_KEY_ID) {
        this.gen_LAST_META_KEY_ID = arg_LAST_META_KEY_ID;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getFollowPan() {
        return this.gen_followPan;
    }

    public void setFollowPan(boolean arg_followPan) {
        this.gen_followPan = arg_followPan;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getUserName() {
        return this.gen_userName;
    }

    public void setUserName(String arg_userName) {
        this.gen_userName = arg_userName;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getSecureConnectionType() {
        return this.gen_secureConnectionType;
    }

    public void setSecureConnectionType(String arg_secureConnectionType) {
        this.gen_secureConnectionType = arg_secureConnectionType;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getShowZoomButtons() {
        return this.gen_showZoomButtons;
    }

    public void setShowZoomButtons(boolean arg_showZoomButtons) {
        this.gen_showZoomButtons = arg_showZoomButtons;
    }

    @Override // android.androidVNC.IConnectionBean
    public String getDoubleTapActionAsString() {
        return this.gen_DOUBLE_TAP_ACTION;
    }

    public void setDoubleTapActionAsString(String arg_DOUBLE_TAP_ACTION) {
        this.gen_DOUBLE_TAP_ACTION = arg_DOUBLE_TAP_ACTION;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getUseImmersive() {
        return this.gen_useImmersive;
    }

    public void setUseImmersive(boolean arg_useImmersive) {
        this.gen_useImmersive = arg_useImmersive;
    }

    @Override // android.androidVNC.IConnectionBean
    public boolean getUseWakeLock() {
        return this.gen_useWakeLock;
    }

    public void setUseWakeLock(boolean arg_useWakeLock) {
        this.gen_useWakeLock = arg_useWakeLock;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public ContentValues Gen_getValues() {
        ContentValues values = new ContentValues();
        values.put("_id", Long.toString(this.gen__Id));
        values.put(GEN_FIELD_NICKNAME, this.gen_nickname);
        values.put(GEN_FIELD_ADDRESS, this.gen_address);
        values.put(GEN_FIELD_PORT, Integer.toString(this.gen_port));
        values.put(GEN_FIELD_PASSWORD, this.gen_password);
        values.put(GEN_FIELD_COLORMODEL, this.gen_colorModel);
        values.put(GEN_FIELD_FORCEFULL, Long.toString(this.gen_forceFull));
        values.put(GEN_FIELD_REPEATERID, this.gen_repeaterId);
        values.put(GEN_FIELD_INPUTMODE, this.gen_inputMode);
        values.put(GEN_FIELD_SCALEMODE, this.gen_SCALEMODE);
        values.put(GEN_FIELD_USELOCALCURSOR, this.gen_useLocalCursor ? "1" : "0");
        values.put(GEN_FIELD_KEEPPASSWORD, this.gen_keepPassword ? "1" : "0");
        values.put(GEN_FIELD_FOLLOWMOUSE, this.gen_followMouse ? "1" : "0");
        values.put(GEN_FIELD_USEREPEATER, this.gen_useRepeater ? "1" : "0");
        values.put("METALISTID", Long.toString(this.gen_metaListId));
        values.put(GEN_FIELD_LAST_META_KEY_ID, Long.toString(this.gen_LAST_META_KEY_ID));
        values.put(GEN_FIELD_FOLLOWPAN, this.gen_followPan ? "1" : "0");
        values.put(GEN_FIELD_USERNAME, this.gen_userName);
        values.put(GEN_FIELD_SECURECONNECTIONTYPE, this.gen_secureConnectionType);
        values.put(GEN_FIELD_SHOWZOOMBUTTONS, this.gen_showZoomButtons ? "1" : "0");
        values.put(GEN_FIELD_DOUBLE_TAP_ACTION, this.gen_DOUBLE_TAP_ACTION);
        values.put(GEN_FIELD_USEIMMERSIVE, this.gen_useImmersive ? "1" : "0");
        values.put(GEN_FIELD_USEWAKELOCK, this.gen_useWakeLock ? "1" : "0");
        return values;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public int[] Gen_columnIndices(Cursor cursor) {
        int[] result = new int[23];
        result[0] = cursor.getColumnIndex("_id");
        if (result[0] == -1) {
            result[0] = cursor.getColumnIndex("_ID");
        }
        result[1] = cursor.getColumnIndex(GEN_FIELD_NICKNAME);
        result[2] = cursor.getColumnIndex(GEN_FIELD_ADDRESS);
        result[3] = cursor.getColumnIndex(GEN_FIELD_PORT);
        result[4] = cursor.getColumnIndex(GEN_FIELD_PASSWORD);
        result[5] = cursor.getColumnIndex(GEN_FIELD_COLORMODEL);
        result[6] = cursor.getColumnIndex(GEN_FIELD_FORCEFULL);
        result[7] = cursor.getColumnIndex(GEN_FIELD_REPEATERID);
        result[8] = cursor.getColumnIndex(GEN_FIELD_INPUTMODE);
        result[9] = cursor.getColumnIndex(GEN_FIELD_SCALEMODE);
        result[10] = cursor.getColumnIndex(GEN_FIELD_USELOCALCURSOR);
        result[11] = cursor.getColumnIndex(GEN_FIELD_KEEPPASSWORD);
        result[12] = cursor.getColumnIndex(GEN_FIELD_FOLLOWMOUSE);
        result[13] = cursor.getColumnIndex(GEN_FIELD_USEREPEATER);
        result[14] = cursor.getColumnIndex("METALISTID");
        result[15] = cursor.getColumnIndex(GEN_FIELD_LAST_META_KEY_ID);
        result[16] = cursor.getColumnIndex(GEN_FIELD_FOLLOWPAN);
        result[17] = cursor.getColumnIndex(GEN_FIELD_USERNAME);
        result[18] = cursor.getColumnIndex(GEN_FIELD_SECURECONNECTIONTYPE);
        result[19] = cursor.getColumnIndex(GEN_FIELD_SHOWZOOMBUTTONS);
        result[20] = cursor.getColumnIndex(GEN_FIELD_DOUBLE_TAP_ACTION);
        result[21] = cursor.getColumnIndex(GEN_FIELD_USEIMMERSIVE);
        result[22] = cursor.getColumnIndex(GEN_FIELD_USEWAKELOCK);
        return result;
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(Cursor cursor, int[] columnIndices) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7 = true;
        if (columnIndices[0] >= 0 && !cursor.isNull(columnIndices[0])) {
            this.gen__Id = cursor.getLong(columnIndices[0]);
        }
        if (columnIndices[1] >= 0 && !cursor.isNull(columnIndices[1])) {
            this.gen_nickname = cursor.getString(columnIndices[1]);
        }
        if (columnIndices[2] >= 0 && !cursor.isNull(columnIndices[2])) {
            this.gen_address = cursor.getString(columnIndices[2]);
        }
        if (columnIndices[3] >= 0 && !cursor.isNull(columnIndices[3])) {
            this.gen_port = cursor.getInt(columnIndices[3]);
        }
        if (columnIndices[4] >= 0 && !cursor.isNull(columnIndices[4])) {
            this.gen_password = cursor.getString(columnIndices[4]);
        }
        if (columnIndices[5] >= 0 && !cursor.isNull(columnIndices[5])) {
            this.gen_colorModel = cursor.getString(columnIndices[5]);
        }
        if (columnIndices[6] >= 0 && !cursor.isNull(columnIndices[6])) {
            this.gen_forceFull = cursor.getLong(columnIndices[6]);
        }
        if (columnIndices[7] >= 0 && !cursor.isNull(columnIndices[7])) {
            this.gen_repeaterId = cursor.getString(columnIndices[7]);
        }
        if (columnIndices[8] >= 0 && !cursor.isNull(columnIndices[8])) {
            this.gen_inputMode = cursor.getString(columnIndices[8]);
        }
        if (columnIndices[9] >= 0 && !cursor.isNull(columnIndices[9])) {
            this.gen_SCALEMODE = cursor.getString(columnIndices[9]);
        }
        if (columnIndices[10] >= 0 && !cursor.isNull(columnIndices[10])) {
            this.gen_useLocalCursor = cursor.getInt(columnIndices[10]) != 0;
        }
        if (columnIndices[11] >= 0 && !cursor.isNull(columnIndices[11])) {
            if (cursor.getInt(columnIndices[11]) != 0) {
                z6 = true;
            } else {
                z6 = false;
            }
            this.gen_keepPassword = z6;
        }
        if (columnIndices[12] >= 0 && !cursor.isNull(columnIndices[12])) {
            if (cursor.getInt(columnIndices[12]) != 0) {
                z5 = true;
            } else {
                z5 = false;
            }
            this.gen_followMouse = z5;
        }
        if (columnIndices[13] >= 0 && !cursor.isNull(columnIndices[13])) {
            if (cursor.getInt(columnIndices[13]) != 0) {
                z4 = true;
            } else {
                z4 = false;
            }
            this.gen_useRepeater = z4;
        }
        if (columnIndices[14] >= 0 && !cursor.isNull(columnIndices[14])) {
            this.gen_metaListId = cursor.getLong(columnIndices[14]);
        }
        if (columnIndices[15] >= 0 && !cursor.isNull(columnIndices[15])) {
            this.gen_LAST_META_KEY_ID = cursor.getLong(columnIndices[15]);
        }
        if (columnIndices[16] >= 0 && !cursor.isNull(columnIndices[16])) {
            if (cursor.getInt(columnIndices[16]) != 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            this.gen_followPan = z3;
        }
        if (columnIndices[17] >= 0 && !cursor.isNull(columnIndices[17])) {
            this.gen_userName = cursor.getString(columnIndices[17]);
        }
        if (columnIndices[18] >= 0 && !cursor.isNull(columnIndices[18])) {
            this.gen_secureConnectionType = cursor.getString(columnIndices[18]);
        }
        if (columnIndices[19] >= 0 && !cursor.isNull(columnIndices[19])) {
            if (cursor.getInt(columnIndices[19]) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.gen_showZoomButtons = z2;
        }
        if (columnIndices[20] >= 0 && !cursor.isNull(columnIndices[20])) {
            this.gen_DOUBLE_TAP_ACTION = cursor.getString(columnIndices[20]);
        }
        if (columnIndices[21] >= 0 && !cursor.isNull(columnIndices[21])) {
            if (cursor.getInt(columnIndices[21]) != 0) {
                z = true;
            } else {
                z = false;
            }
            this.gen_useImmersive = z;
        }
        if (columnIndices[22] >= 0 && !cursor.isNull(columnIndices[22])) {
            if (cursor.getInt(columnIndices[22]) == 0) {
                z7 = false;
            }
            this.gen_useWakeLock = z7;
        }
    }

    @Override // com.antlersoft.android.dbimpl.ImplementationBase
    public void Gen_populate(ContentValues values) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7 = true;
        this.gen__Id = values.getAsLong("_id").longValue();
        this.gen_nickname = values.getAsString(GEN_FIELD_NICKNAME);
        this.gen_address = values.getAsString(GEN_FIELD_ADDRESS);
        this.gen_port = values.getAsInteger(GEN_FIELD_PORT).intValue();
        this.gen_password = values.getAsString(GEN_FIELD_PASSWORD);
        this.gen_colorModel = values.getAsString(GEN_FIELD_COLORMODEL);
        this.gen_forceFull = values.getAsLong(GEN_FIELD_FORCEFULL).longValue();
        this.gen_repeaterId = values.getAsString(GEN_FIELD_REPEATERID);
        this.gen_inputMode = values.getAsString(GEN_FIELD_INPUTMODE);
        this.gen_SCALEMODE = values.getAsString(GEN_FIELD_SCALEMODE);
        this.gen_useLocalCursor = values.getAsInteger(GEN_FIELD_USELOCALCURSOR).intValue() != 0;
        if (values.getAsInteger(GEN_FIELD_KEEPPASSWORD).intValue() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.gen_keepPassword = z;
        if (values.getAsInteger(GEN_FIELD_FOLLOWMOUSE).intValue() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.gen_followMouse = z2;
        if (values.getAsInteger(GEN_FIELD_USEREPEATER).intValue() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.gen_useRepeater = z3;
        this.gen_metaListId = values.getAsLong("METALISTID").longValue();
        this.gen_LAST_META_KEY_ID = values.getAsLong(GEN_FIELD_LAST_META_KEY_ID).longValue();
        if (values.getAsInteger(GEN_FIELD_FOLLOWPAN).intValue() != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.gen_followPan = z4;
        this.gen_userName = values.getAsString(GEN_FIELD_USERNAME);
        this.gen_secureConnectionType = values.getAsString(GEN_FIELD_SECURECONNECTIONTYPE);
        if (values.getAsInteger(GEN_FIELD_SHOWZOOMBUTTONS).intValue() != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.gen_showZoomButtons = z5;
        this.gen_DOUBLE_TAP_ACTION = values.getAsString(GEN_FIELD_DOUBLE_TAP_ACTION);
        if (values.getAsInteger(GEN_FIELD_USEIMMERSIVE).intValue() != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.gen_useImmersive = z6;
        if (values.getAsInteger(GEN_FIELD_USEWAKELOCK).intValue() == 0) {
            z7 = false;
        }
        this.gen_useWakeLock = z7;
    }
}
