package android.androidVNC;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.antlersoft.android.bc.BCFactory;
import com.antlersoft.android.bc.IBCScaleGestureDetector;
import com.antlersoft.android.zoomer.ZoomControls;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class VncCanvasActivity extends Activity {
    static final String FIT_SCREEN_NAME = "FIT_SCREEN";
    private static final String TAG = "VncCanvasActivity";
    static final String TOUCHPAD_MODE = "TOUCHPAD_MODE";
    static final String TOUCH_ZOOM_MODE = "TOUCH_ZOOM_MODE";
    static final long ZOOM_HIDE_DELAY_MS = 2500;
    private static final int[] inputModeIds = {R.id.itemInputFitToScreen, R.id.itemInputTouchpad, R.id.itemInputMouse, R.id.itemInputPan, R.id.itemInputTouchPanTrackballMouse, R.id.itemInputDPadPanTouchMouse, R.id.itemInputTouchPanZoomMouse};
    private ConnectionBean connection;
    VncDatabase database;
    long hideZoomAfterMs;
    HideZoomRunnable hideZoomInstance = new HideZoomRunnable();
    AbstractInputHandler inputHandler;
    private AbstractInputHandler[] inputModeHandlers;
    private MenuItem[] inputModeMenuItems;
    private MetaKeyBean lastSentKey;
    float panTouchX;
    float panTouchY;
    Panner panner;
    private boolean trackballButtonDown;
    VncCanvas vncCanvas;
    ZoomControls zoomer;

    /* access modifiers changed from: package-private */
    public class ZoomInputHandler extends AbstractGestureInputHandler {
        static final float FLING_FACTOR = 8.0f;
        private boolean dragMode;

        ZoomInputHandler() {
            super(VncCanvasActivity.this);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getString(R.string.input_mode_touch_pan_zoom_mouse);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return VncCanvasActivity.TOUCH_ZOOM_MODE;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            return VncCanvasActivity.this.defaultKeyDownHandler(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            return VncCanvasActivity.this.defaultKeyUpHandler(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return VncCanvasActivity.this.trackballMouse(evt);
        }

        public boolean onDown(MotionEvent e) {
            VncCanvasActivity.this.panner.stop();
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            VncCanvasActivity.this.showZoomer(false);
            VncCanvasActivity.this.panner.start(-(velocityX / FLING_FACTOR), -(velocityY / FLING_FACTOR), new Panner.VelocityUpdater() {
                /* class android.androidVNC.VncCanvasActivity.ZoomInputHandler.C00501 */

                @Override // android.androidVNC.Panner.VelocityUpdater
                public boolean updateVelocity(PointF p, long interval) {
                    double scale = Math.pow(0.8d, ((double) interval) / 50.0d);
                    p.x = (float) (((double) p.x) * scale);
                    p.y = (float) (((double) p.y) * scale);
                    return ((double) Math.abs(p.x)) > 0.5d || ((double) Math.abs(p.y)) > 0.5d;
                }
            });
            return true;
        }

        @Override // android.androidVNC.AbstractGestureInputHandler, android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent e) {
            if (!this.dragMode) {
                return super.onTouchEvent(e);
            }
            VncCanvasActivity.this.vncCanvas.changeTouchCoordinatesToFullFrame(e);
            if (e.getAction() == 1) {
                this.dragMode = false;
            }
            return VncCanvasActivity.this.vncCanvas.processPointerEvent(e, true);
        }

        public void onLongPress(MotionEvent e) {
            VncCanvasActivity.this.showZoomer(true);
            BCFactory.getInstance().getBCHaptic().performLongPressHaptic(VncCanvasActivity.this.vncCanvas);
            this.dragMode = true;
            VncCanvasActivity.this.vncCanvas.processPointerEvent(VncCanvasActivity.this.vncCanvas.changeTouchCoordinatesToFullFrame(e), true);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (this.inScaling) {
                return false;
            }
            VncCanvasActivity.this.showZoomer(false);
            return VncCanvasActivity.this.vncCanvas.pan((int) distanceX, (int) distanceY);
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            VncCanvasActivity.this.vncCanvas.changeTouchCoordinatesToFullFrame(e);
            VncCanvasActivity.this.vncCanvas.processPointerEvent(e, true);
            e.setAction(1);
            return VncCanvasActivity.this.vncCanvas.processPointerEvent(e, false);
        }

        public boolean onDoubleTap(MotionEvent e) {
            VncCanvasActivity.this.vncCanvas.changeTouchCoordinatesToFullFrame(e);
            VncCanvasActivity.this.vncCanvas.processPointerEvent(e, true, true);
            e.setAction(1);
            return VncCanvasActivity.this.vncCanvas.processPointerEvent(e, false, true);
        }
    }

    public class TouchpadInputHandler extends AbstractGestureInputHandler {
        private boolean dragMode;
        float dragX;
        float dragY;
        private DPadMouseKeyHandler keyHandler;

        @Override // android.androidVNC.AbstractGestureInputHandler, com.antlersoft.android.p000bc.OnScaleGestureListener
        public /* bridge */ /* synthetic */ boolean onScale(IBCScaleGestureDetector x0) {
            return super.onScale(x0);
        }

        @Override // android.androidVNC.AbstractGestureInputHandler, com.antlersoft.android.p000bc.OnScaleGestureListener
        public /* bridge */ /* synthetic */ boolean onScaleBegin(IBCScaleGestureDetector x0) {
            return super.onScaleBegin(x0);
        }

        @Override // android.androidVNC.AbstractGestureInputHandler, com.antlersoft.android.p000bc.OnScaleGestureListener
        public /* bridge */ /* synthetic */ void onScaleEnd(IBCScaleGestureDetector x0) {
            super.onScaleEnd(x0);
        }

        TouchpadInputHandler() {
            super(VncCanvasActivity.this);
            this.keyHandler = new DPadMouseKeyHandler(VncCanvasActivity.this, VncCanvasActivity.this.vncCanvas.handler);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getString(R.string.input_mode_touchpad);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return VncCanvasActivity.TOUCHPAD_MODE;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            return this.keyHandler.onKeyDown(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            return this.keyHandler.onKeyUp(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return VncCanvasActivity.this.trackballMouse(evt);
        }

        private float fineCtrlScale(float delta) {
            float delta2;
            float sign = delta > 0.0f ? 1.0f : -1.0f;
            float delta3 = Math.abs(delta);
            if (delta3 >= 1.0f && delta3 <= 3.0f) {
                delta2 = 1.0f;
            } else if (delta3 <= 10.0f) {
                delta2 = (float) (((double) delta3) * 0.34d);
            } else if (delta3 <= 30.0f) {
                delta2 = delta3 * (delta3 / 30.0f);
            } else if (delta3 <= 90.0f) {
                delta2 = delta3 * (delta3 / 30.0f);
            } else {
                delta2 = (float) (((double) delta3) * 3.0d);
            }
            return sign * delta2;
        }

        public void onLongPress(MotionEvent e) {
            VncCanvasActivity.this.showZoomer(true);
            BCFactory.getInstance().getBCHaptic().performLongPressHaptic(VncCanvasActivity.this.vncCanvas);
            this.dragMode = true;
            this.dragX = e.getX();
            this.dragY = e.getY();
            remoteMouseStayPut(e);
            VncCanvasActivity.this.vncCanvas.processPointerEvent(e, true);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (BCFactory.getInstance().getBCMotionEvent().getPointerCount(e2) <= 1) {
                float deltaX = fineCtrlScale((-distanceX) * VncCanvasActivity.this.vncCanvas.getScale());
                float deltaY = fineCtrlScale((-distanceY) * VncCanvasActivity.this.vncCanvas.getScale());
                float newRemoteX = ((float) VncCanvasActivity.this.vncCanvas.mouseX) + deltaX;
                float newRemoteY = ((float) VncCanvasActivity.this.vncCanvas.mouseY) + deltaY;
                if (this.dragMode) {
                    if (e2.getAction() == 1) {
                        this.dragMode = false;
                    }
                    this.dragX = e2.getX();
                    this.dragY = e2.getY();
                    e2.setLocation(newRemoteX, newRemoteY);
                    return VncCanvasActivity.this.vncCanvas.processPointerEvent(e2, true);
                }
                e2.setLocation(newRemoteX, newRemoteY);
                VncCanvasActivity.this.vncCanvas.processPointerEvent(e2, false);
                return true;
            } else if (this.inScaling) {
                return false;
            } else {
                VncCanvasActivity.this.showZoomer(false);
                return VncCanvasActivity.this.vncCanvas.pan((int) distanceX, (int) distanceY);
            }
        }

        @Override // android.androidVNC.AbstractGestureInputHandler, android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent e) {
            if (!this.dragMode) {
                return super.onTouchEvent(e);
            }
            float deltaX = (e.getX() - this.dragX) * VncCanvasActivity.this.vncCanvas.getScale();
            float deltaY = (e.getY() - this.dragY) * VncCanvasActivity.this.vncCanvas.getScale();
            this.dragX = e.getX();
            this.dragY = e.getY();
            float deltaX2 = fineCtrlScale(deltaX);
            float deltaY2 = fineCtrlScale(deltaY);
            float newRemoteX = ((float) VncCanvasActivity.this.vncCanvas.mouseX) + deltaX2;
            float newRemoteY = ((float) VncCanvasActivity.this.vncCanvas.mouseY) + deltaY2;
            if (e.getAction() == 1) {
                this.dragMode = false;
            }
            e.setLocation(newRemoteX, newRemoteY);
            return VncCanvasActivity.this.vncCanvas.processPointerEvent(e, true);
        }

        private void remoteMouseStayPut(MotionEvent e) {
            e.setLocation((float) VncCanvasActivity.this.vncCanvas.mouseX, (float) VncCanvasActivity.this.vncCanvas.mouseY);
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            boolean multiTouch;
            boolean z;
            boolean z2 = true;
            if (BCFactory.getInstance().getBCMotionEvent().getPointerCount(e) > 1) {
                multiTouch = true;
            } else {
                multiTouch = false;
            }
            remoteMouseStayPut(e);
            VncCanvas vncCanvas = VncCanvasActivity.this.vncCanvas;
            if (multiTouch || VncCanvasActivity.this.vncCanvas.cameraButtonDown) {
                z = true;
            } else {
                z = false;
            }
            vncCanvas.processPointerEvent(e, true, z);
            e.setAction(1);
            VncCanvas vncCanvas2 = VncCanvasActivity.this.vncCanvas;
            if (!multiTouch && !VncCanvasActivity.this.vncCanvas.cameraButtonDown) {
                z2 = false;
            }
            return vncCanvas2.processPointerEvent(e, false, z2);
        }

        public boolean onDoubleTap(MotionEvent e) {
            remoteMouseStayPut(e);
            VncCanvasActivity.this.vncCanvas.processPointerEvent(e, true, true);
            e.setAction(1);
            return VncCanvasActivity.this.vncCanvas.processPointerEvent(e, false, true);
        }

        public boolean onDown(MotionEvent e) {
            VncCanvasActivity.this.panner.stop();
            return true;
        }
    }

    public void onCreate(Bundle icicle) {
        int port;
        MostRecentBean bean;
        super.onCreate(icicle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.database = new VncDatabase(this);
        Intent i = getIntent();
        this.connection = new ConnectionBean();
        Uri data = i.getData();
        if (data == null || !data.getScheme().equals("vnc")) {
            Bundle extras = i.getExtras();
            if (extras != null) {
                this.connection.Gen_populate((ContentValues) extras.getParcelable(VncConstants.CONNECTION));
            }
            if (this.connection.getPort() == 0) {
                this.connection.setPort(5900);
            }
            String host = this.connection.getAddress();
            if (host.indexOf(58) > -1) {
                try {
                    this.connection.setPort(Integer.parseInt(host.substring(host.indexOf(58) + 1)));
                } catch (Exception e) {
                }
                this.connection.setAddress(host.substring(0, host.indexOf(58)));
            }
        } else {
            String host2 = data.getHost();
            int index = host2.indexOf(58);
            if (index != -1) {
                try {
                    port = Integer.parseInt(host2.substring(index + 1));
                } catch (NumberFormatException e2) {
                    port = 0;
                }
                host2 = host2.substring(0, index);
            } else {
                port = data.getPort();
            }
            if (!host2.equals(VncConstants.CONNECTION)) {
                this.connection.setAddress(host2);
                this.connection.setNickname(this.connection.getAddress());
                this.connection.setPort(port);
                List<String> path = data.getPathSegments();
                if (path.size() >= 1) {
                    this.connection.setColorModel(path.get(0));
                }
                if (path.size() >= 2) {
                    this.connection.setPassword(path.get(1));
                }
                this.connection.save(this.database.getWritableDatabase());
            } else if (this.connection.Gen_read(this.database.getReadableDatabase(), (long) port) && (bean = AndroidVNC.getMostRecent(this.database.getReadableDatabase())) != null) {
                bean.setConnectionId(this.connection.get_Id());
                bean.Gen_update(this.database.getWritableDatabase());
            }
        }
        setContentView(R.layout.canvas);
        this.vncCanvas = (VncCanvas) findViewById(R.id.vnc_canvas);
        this.zoomer = (ZoomControls) findViewById(R.id.zoomer);
        this.vncCanvas.initializeVncCanvas(this.connection, new Runnable() {
            /* class android.androidVNC.VncCanvasActivity.RunnableC00431 */

            public void run() {
                VncCanvasActivity.this.setModes();
            }
        });
        this.zoomer.hide();
        this.zoomer.setOnZoomInClickListener(new View.OnClickListener() {
            /* class android.androidVNC.VncCanvasActivity.View$OnClickListenerC00442 */

            public void onClick(View v) {
                VncCanvasActivity.this.showZoomer(true);
                VncCanvasActivity.this.vncCanvas.scaling.zoomIn(VncCanvasActivity.this);
            }
        });
        this.zoomer.setOnZoomOutClickListener(new View.OnClickListener() {
            /* class android.androidVNC.VncCanvasActivity.View$OnClickListenerC00453 */

            public void onClick(View v) {
                VncCanvasActivity.this.showZoomer(true);
                VncCanvasActivity.this.vncCanvas.scaling.zoomOut(VncCanvasActivity.this);
            }
        });
        this.zoomer.setOnZoomKeyboardClickListener(new View.OnClickListener() {
            /* class android.androidVNC.VncCanvasActivity.View$OnClickListenerC00464 */

            public void onClick(View v) {
                ((InputMethodManager) VncCanvasActivity.this.getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, 0);
            }
        });
        this.panner = new Panner(this, this.vncCanvas.handler);
        this.inputHandler = getInputHandlerById(R.id.itemInputFitToScreen);

        // FULLSCREEN
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // NO TIMEOUT
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /* access modifiers changed from: package-private */
    public void setModes() {
        AbstractInputHandler handler = getInputHandlerByName(this.connection.getInputMode());
        AbstractScaling.getByScaleType(this.connection.getScaleMode()).setScaleTypeForActivity(this);
        this.inputHandler = handler;
        showPanningState();
        if (this.connection.getScaleMode() == ImageView.ScaleType.MATRIX && this.connection.getUseImmersive()) {
            BCFactory.getInstance().getSystemUiVisibility().HideSystemUI(this.vncCanvas);
        }
    }

    /* access modifiers changed from: package-private */
    public ConnectionBean getConnection() {
        return this.connection;
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case R.layout.entertext /*{ENCODED_INT: 2130903042}*/:
                return new EnterTextDialog(this);
            default:
                return new MetaKeyDialog(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (dialog instanceof ConnectionSettable) {
            ((ConnectionSettable) dialog).setConnection(this.connection);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && this.connection.getScaleMode() == ImageView.ScaleType.MATRIX && this.connection.getUseImmersive()) {
            BCFactory.getInstance().getSystemUiVisibility().HideSystemUI(this.vncCanvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.vncCanvas.disableRepaints();
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        this.vncCanvas.enableRepaints();
        super.onRestart();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vnccanvasactivitymenu, menu);
        if (this.vncCanvas.scaling != null) {
            menu.findItem(this.vncCanvas.scaling.getId()).setChecked(true);
        }
        Menu inputMenu = menu.findItem(R.id.itemInputMode).getSubMenu();
        this.inputModeMenuItems = new MenuItem[inputModeIds.length];
        for (int i = 0; i < inputModeIds.length; i++) {
            this.inputModeMenuItems[i] = inputMenu.findItem(inputModeIds[i]);
        }
        updateInputMenu();
        menu.findItem(R.id.itemFollowMouse).setChecked(this.connection.getFollowMouse());
        menu.findItem(R.id.itemFollowPan).setChecked(this.connection.getFollowPan());
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateInputMenu() {
        if (!(this.inputModeMenuItems == null || this.vncCanvas.scaling == null)) {
            MenuItem[] menuItemArr = this.inputModeMenuItems;
            for (MenuItem item : menuItemArr) {
                item.setEnabled(this.vncCanvas.scaling.isValidInputMode(item.getItemId()));
                if (getInputHandlerById(item.getItemId()) == this.inputHandler) {
                    item.setChecked(true);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public AbstractInputHandler getInputHandlerById(int id) {
        if (this.inputModeHandlers == null) {
            this.inputModeHandlers = new AbstractInputHandler[inputModeIds.length];
        }
        for (int i = 0; i < inputModeIds.length; i++) {
            if (inputModeIds[i] == id) {
                if (this.inputModeHandlers[i] == null) {
                    switch (id) {
                        case R.id.itemInputTouchPanZoomMouse /*{ENCODED_INT: 2131099717}*/:
                            this.inputModeHandlers[i] = new ZoomInputHandler();
                            break;
                        case R.id.itemInputTouchpad /*{ENCODED_INT: 2131099718}*/:
                            this.inputModeHandlers[i] = new TouchpadInputHandler();
                            break;
                        case R.id.itemInputFitToScreen /*{ENCODED_INT: 2131099719}*/:
                            this.inputModeHandlers[i] = new FitToScreenMode();
                            break;
                        case R.id.itemInputPan /*{ENCODED_INT: 2131099720}*/:
                            this.inputModeHandlers[i] = new PanMode();
                            break;
                        case R.id.itemInputMouse /*{ENCODED_INT: 2131099721}*/:
                            this.inputModeHandlers[i] = new MouseMode();
                            break;
                        case R.id.itemInputTouchPanTrackballMouse /*{ENCODED_INT: 2131099722}*/:
                            this.inputModeHandlers[i] = new TouchPanTrackballMouse();
                            break;
                        case R.id.itemInputDPadPanTouchMouse /*{ENCODED_INT: 2131099723}*/:
                            this.inputModeHandlers[i] = new DPadPanTouchMouseMode();
                            break;
                    }
                }
                return this.inputModeHandlers[i];
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public AbstractInputHandler getInputHandlerByName(String name) {
        AbstractInputHandler result = null;
        int[] iArr = inputModeIds;
        int length = iArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            AbstractInputHandler handler = getInputHandlerById(iArr[i]);
            if (handler.getName().equals(name)) {
                result = handler;
                break;
            }
            i++;
        }
        if (result == null) {
            return getInputHandlerById(R.id.itemInputTouchPanZoomMouse);
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public int getModeIdFromHandler(AbstractInputHandler handler) {
        int[] iArr = inputModeIds;
        for (int id : iArr) {
            if (handler == getInputHandlerById(id)) {
                return id;
            }
        }
        return R.id.itemInputTouchPanZoomMouse;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean newFollowPan;
        boolean newFollow = false;
        this.vncCanvas.afterMenu = true;
        switch (item.getItemId()) {
            case R.id.itemOpenDoc /*{ENCODED_INT: 2131099708}*/:
                Utils.showDocumentation(this);
                return true;
            case R.id.itemDontShowAgain /*{ENCODED_INT: 2131099709}*/:
            case R.id.itemClose /*{ENCODED_INT: 2131099710}*/:
            case R.id.itemDeleteKeyList /*{ENCODED_INT: 2131099711}*/:
            case R.id.itemDeleteKey /*{ENCODED_INT: 2131099712}*/:
            case R.id.itemInputMode /*{ENCODED_INT: 2131099716}*/:
            case R.id.itemInputTouchPanZoomMouse /*{ENCODED_INT: 2131099717}*/:
            case R.id.itemInputTouchpad /*{ENCODED_INT: 2131099718}*/:
            case R.id.itemInputFitToScreen /*{ENCODED_INT: 2131099719}*/:
            case R.id.itemInputPan /*{ENCODED_INT: 2131099720}*/:
            case R.id.itemInputMouse /*{ENCODED_INT: 2131099721}*/:
            case R.id.itemInputTouchPanTrackballMouse /*{ENCODED_INT: 2131099722}*/:
            case R.id.itemInputDPadPanTouchMouse /*{ENCODED_INT: 2131099723}*/:
            case R.id.itemScaling /*{ENCODED_INT: 2131099728}*/:
            case R.id.groupScaling /*{ENCODED_INT: 2131099729}*/:
            default:
                AbstractInputHandler input = getInputHandlerById(item.getItemId());
                if (input == null) {
                    return super.onOptionsItemSelected(item);
                }
                this.inputHandler = input;
                this.connection.setInputMode(input.getName());
                if (input.getName().equals(TOUCHPAD_MODE)) {
                    this.connection.setFollowMouse(true);
                }
                item.setChecked(true);
                showPanningState();
                this.connection.save(this.database.getWritableDatabase());
                return true;
            case R.id.itemSpecialKeys /*{ENCODED_INT: 2131099713}*/:
                showDialog(R.layout.metakey);
                return true;
            case R.id.itemCenterMouse /*{ENCODED_INT: 2131099714}*/:
                this.vncCanvas.warpMouse(this.vncCanvas.absoluteXPosition + (this.vncCanvas.getVisibleWidth() / 2), this.vncCanvas.absoluteYPosition + (this.vncCanvas.getVisibleHeight() / 2));
                return true;
            case R.id.itemEnterText /*{ENCODED_INT: 2131099715}*/:
                showDialog(R.layout.entertext);
                return true;
            case R.id.itemDisconnect /*{ENCODED_INT: 2131099724}*/:
                this.vncCanvas.closeConnection();
                finish();
                return true;
            case R.id.itemFollowMouse /*{ENCODED_INT: 2131099725}*/:
                if (!this.connection.getFollowMouse()) {
                    newFollow = true;
                }
                item.setChecked(newFollow);
                this.connection.setFollowMouse(newFollow);
                if (newFollow) {
                    this.vncCanvas.panToMouse();
                }
                this.connection.save(this.database.getWritableDatabase());
                return true;
            case R.id.itemFollowPan /*{ENCODED_INT: 2131099726}*/:
                if (!this.connection.getFollowPan()) {
                    newFollowPan = true;
                } else {
                    newFollowPan = false;
                }
                item.setChecked(newFollowPan);
                this.connection.setFollowPan(newFollowPan);
                this.connection.save(this.database.getWritableDatabase());
                return true;
            case R.id.itemColorMode /*{ENCODED_INT: 2131099727}*/:
                selectColorModel();
                return true;
            case R.id.itemZoomable /*{ENCODED_INT: 2131099730}*/:
            case R.id.itemOneToOne /*{ENCODED_INT: 2131099731}*/:
            case R.id.itemFitToScreen /*{ENCODED_INT: 2131099732}*/:
                AbstractScaling.getById(item.getItemId()).setScaleTypeForActivity(this);
                item.setChecked(true);
                showPanningState();
                return true;
            case R.id.itemCtrlAltDel /*{ENCODED_INT: 2131099733}*/:
                this.vncCanvas.sendMetaKey(MetaKeyBean.keyCtrlAltDel);
                return true;
            case R.id.itemInfo /*{ENCODED_INT: 2131099734}*/:
                this.vncCanvas.showConnectionInfo();
                return true;
            case R.id.itemSendKeyAgain /*{ENCODED_INT: 2131099735}*/:
                sendSpecialKeyAgain();
                return true;
            case R.id.itemArrowLeft /*{ENCODED_INT: 2131099736}*/:
                this.vncCanvas.sendMetaKey(MetaKeyBean.keyArrowLeft);
                return true;
            case R.id.itemArrowDown /*{ENCODED_INT: 2131099737}*/:
                this.vncCanvas.sendMetaKey(MetaKeyBean.keyArrowDown);
                return true;
            case R.id.itemArrowUp /*{ENCODED_INT: 2131099738}*/:
                this.vncCanvas.sendMetaKey(MetaKeyBean.keyArrowUp);
                return true;
            case R.id.itemArrowRight /*{ENCODED_INT: 2131099739}*/:
                this.vncCanvas.sendMetaKey(MetaKeyBean.keyArrowRight);
                return true;
        }
    }

    public void onOptionsMenuClosed(Menu menu) {
        if (this.connection.getUseImmersive()) {
            BCFactory.getInstance().getSystemUiVisibility().HideSystemUI(this.vncCanvas);
        }
        super.onOptionsMenuClosed(menu);
    }

    private void sendSpecialKeyAgain() {
        if (this.lastSentKey == null || this.lastSentKey.get_Id() != this.connection.getLastMetaKeyId()) {
            ArrayList<MetaKeyBean> keys = new ArrayList<>();
            Cursor c = this.database.getReadableDatabase().rawQuery(MessageFormat.format("SELECT * FROM {0} WHERE {1} = {2}", AbstractMetaKeyBean.GEN_TABLE_NAME, "_id", Long.valueOf(this.connection.getLastMetaKeyId())), MetaKeyDialog.EMPTY_ARGS);
            MetaKeyBean.Gen_populateFromCursor(c, keys, MetaKeyBean.NEW);
            c.close();
            if (keys.size() > 0) {
                this.lastSentKey = keys.get(0);
            } else {
                this.lastSentKey = null;
            }
        }
        if (this.lastSentKey != null) {
            this.vncCanvas.sendMetaKey(this.lastSentKey);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            this.vncCanvas.closeConnection();
            this.vncCanvas.onDestroy();
            this.database.close();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent evt) {
        if (keyCode == 82) {
            return super.onKeyDown(keyCode, evt);
        }
        return this.inputHandler.onKeyDown(keyCode, evt);
    }

    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        if (keyCode == 82) {
            return super.onKeyUp(keyCode, evt);
        }
        return this.inputHandler.onKeyUp(keyCode, evt);
    }

    public void showPanningState() {
        Toast.makeText(this, this.inputHandler.getHandlerDescription(), Toast.LENGTH_LONG).show();
    }

    public boolean onTrackballEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.trackballButtonDown = true;
                break;
            case 1:
                this.trackballButtonDown = false;
                break;
        }
        return this.inputHandler.onTrackballEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.inputHandler.onTouchEvent(event);
    }

    private void selectColorModel() {
        this.vncCanvas.disableRepaints();
        String[] choices = new String[COLORMODEL.values().length];
        int currentSelection = -1;
        for (int i = 0; i < choices.length; i++) {
            COLORMODEL cm = COLORMODEL.values()[i];
            choices[i] = cm.toString();
            if (this.vncCanvas.isColorModel(cm)) {
                currentSelection = i;
            }
        }
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        ListView list = new ListView(this);
        list.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.connection_list, choices));
        list.setChoiceMode(1);
        list.setItemChecked(currentSelection, true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                dialog.dismiss();
                COLORMODEL cm = COLORMODEL.values()[arg2];
                VncCanvasActivity.this.vncCanvas.setColorModel(cm);
                VncCanvasActivity.this.connection.setColorModel(cm.nameString());
                VncCanvasActivity.this.connection.save(VncCanvasActivity.this.database.getWritableDatabase());
                Toast.makeText(VncCanvasActivity.this, "Updating Color Model to " + cm.toString(), Toast.LENGTH_LONG).show();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            public void onDismiss(DialogInterface arg0) {
                Log.i(VncCanvasActivity.TAG, "Color Model Selector dismissed");
                VncCanvasActivity.this.vncCanvas.enableRepaints();
            }
        });
        dialog.setContentView(list);
        dialog.show();
    }

    private boolean pan(MotionEvent event) {
        float curX = event.getX();
        float curY = event.getY();
        return this.vncCanvas.pan((int) (this.panTouchX - curX), (int) (this.panTouchY - curY));
    }

    /* access modifiers changed from: package-private */
    public boolean defaultKeyDownHandler(int keyCode, KeyEvent evt) {
        if (this.vncCanvas.processLocalKeyEvent(keyCode, evt)) {
            return true;
        }
        return super.onKeyDown(keyCode, evt);
    }

    /* access modifiers changed from: package-private */
    public boolean defaultKeyUpHandler(int keyCode, KeyEvent evt) {
        if (this.vncCanvas.processLocalKeyEvent(keyCode, evt)) {
            return true;
        }
        return super.onKeyUp(keyCode, evt);
    }

    /* access modifiers changed from: package-private */
    public boolean touchPan(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.panTouchX = event.getX();
                this.panTouchY = event.getY();
                return true;
            case 1:
                pan(event);
                return true;
            case 2:
                pan(event);
                this.panTouchX = event.getX();
                this.panTouchY = event.getY();
                return true;
            default:
                return true;
        }
    }

    private static int convertTrackballDelta(double delta) {
        return (delta < 0.0d ? -1 : 1) * ((int) Math.pow(Math.abs(delta) * 6.01d, 2.5d));
    }

    /* access modifiers changed from: package-private */
    public boolean trackballMouse(MotionEvent evt) {
        evt.offsetLocation(((float) (this.vncCanvas.mouseX + convertTrackballDelta((double) evt.getX()))) - evt.getX(), ((float) (this.vncCanvas.mouseY + convertTrackballDelta((double) evt.getY()))) - evt.getY());
        if (this.vncCanvas.processPointerEvent(evt, this.trackballButtonDown)) {
            return true;
        }
        return super.onTouchEvent(evt);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showZoomer(boolean force) {
        if (force || this.zoomer.getVisibility() != View.INVISIBLE) {
            this.zoomer.show();
            this.hideZoomAfterMs = SystemClock.uptimeMillis() + ZOOM_HIDE_DELAY_MS;
            this.vncCanvas.handler.postAtTime(this.hideZoomInstance, this.hideZoomAfterMs + 10);
        }
    }

    /* access modifiers changed from: private */
    public class HideZoomRunnable implements Runnable {
        private HideZoomRunnable() {
        }

        public void run() {
            if (SystemClock.uptimeMillis() >= VncCanvasActivity.this.hideZoomAfterMs) {
                VncCanvasActivity.this.zoomer.hide();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class PanMode implements AbstractInputHandler {
        PanMode() {
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            switch (keyCode) {
                case AbstractConnectionBean.GEN_ID_SHOWZOOMBUTTONS /*{ENCODED_INT: 19}*/:
                    onTouchEvent(MotionEvent.obtain(1, System.currentTimeMillis(), 2, VncCanvasActivity.this.panTouchX, VncCanvasActivity.this.panTouchY + 100.0f, 0));
                    return true;
                case 20:
                    onTouchEvent(MotionEvent.obtain(1, System.currentTimeMillis(), 2, VncCanvasActivity.this.panTouchX, VncCanvasActivity.this.panTouchY - 100.0f, 0));
                    return true;
                case AbstractConnectionBean.GEN_ID_USEIMMERSIVE /*{ENCODED_INT: 21}*/:
                    onTouchEvent(MotionEvent.obtain(1, System.currentTimeMillis(), 2, VncCanvasActivity.this.panTouchX + 100.0f, VncCanvasActivity.this.panTouchY, 0));
                    return true;
                case AbstractConnectionBean.GEN_ID_USEWAKELOCK /*{ENCODED_INT: 22}*/:
                    onTouchEvent(MotionEvent.obtain(1, System.currentTimeMillis(), 2, VncCanvasActivity.this.panTouchX - 100.0f, VncCanvasActivity.this.panTouchY, 0));
                    return true;
                case AbstractConnectionBean.GEN_COUNT /*{ENCODED_INT: 23}*/:
                    return true;
                default:
                    return VncCanvasActivity.this.defaultKeyDownHandler(keyCode, evt);
            }
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            switch (keyCode) {
                case AbstractConnectionBean.GEN_ID_SHOWZOOMBUTTONS /*{ENCODED_INT: 19}*/:
                case 20:
                case AbstractConnectionBean.GEN_ID_USEIMMERSIVE /*{ENCODED_INT: 21}*/:
                case AbstractConnectionBean.GEN_ID_USEWAKELOCK /*{ENCODED_INT: 22}*/:
                    return true;
                case AbstractConnectionBean.GEN_COUNT /*{ENCODED_INT: 23}*/:
                    VncCanvasActivity.this.inputHandler = VncCanvasActivity.this.getInputHandlerById(R.id.itemInputMouse);
                    VncCanvasActivity.this.connection.setInputMode(VncCanvasActivity.this.inputHandler.getName());
                    VncCanvasActivity.this.connection.save(VncCanvasActivity.this.database.getWritableDatabase());
                    VncCanvasActivity.this.updateInputMenu();
                    VncCanvasActivity.this.showPanningState();
                    return true;
                default:
                    return VncCanvasActivity.this.defaultKeyUpHandler(keyCode, evt);
            }
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent event) {
            return VncCanvasActivity.this.touchPan(event);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return false;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getText(R.string.input_mode_panning);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return "PAN_MODE";
        }
    }

    public class TouchPanTrackballMouse implements AbstractInputHandler {
        private DPadMouseKeyHandler keyHandler = new DPadMouseKeyHandler(VncCanvasActivity.this, VncCanvasActivity.this.vncCanvas.handler);

        public TouchPanTrackballMouse() {
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            return this.keyHandler.onKeyDown(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            return this.keyHandler.onKeyUp(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent evt) {
            return VncCanvasActivity.this.touchPan(evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return VncCanvasActivity.this.trackballMouse(evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getText(R.string.input_mode_touchpad_pan_trackball_mouse);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return "TOUCH_PAN_TRACKBALL_MOUSE";
        }
    }

    public class FitToScreenMode implements AbstractInputHandler {
        private DPadMouseKeyHandler keyHandler = new DPadMouseKeyHandler(VncCanvasActivity.this, VncCanvasActivity.this.vncCanvas.handler);

        public FitToScreenMode() {
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            return this.keyHandler.onKeyDown(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            return this.keyHandler.onKeyUp(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent evt) {
            return false;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return VncCanvasActivity.this.trackballMouse(evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getText(R.string.input_mode_fit_to_screen);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return VncCanvasActivity.FIT_SCREEN_NAME;
        }
    }

    /* access modifiers changed from: package-private */
    public class MouseMode implements AbstractInputHandler {
        MouseMode() {
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            if (keyCode == 23) {
                return true;
            }
            return VncCanvasActivity.this.defaultKeyDownHandler(keyCode, evt);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            if (keyCode != 23) {
                return VncCanvasActivity.this.defaultKeyUpHandler(keyCode, evt);
            }
            VncCanvasActivity.this.inputHandler = VncCanvasActivity.this.getInputHandlerById(R.id.itemInputPan);
            VncCanvasActivity.this.showPanningState();
            VncCanvasActivity.this.connection.setInputMode(VncCanvasActivity.this.inputHandler.getName());
            VncCanvasActivity.this.connection.save(VncCanvasActivity.this.database.getWritableDatabase());
            VncCanvasActivity.this.updateInputMenu();
            return true;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent event) {
            VncCanvasActivity.this.vncCanvas.changeTouchCoordinatesToFullFrame(event);
            if (VncCanvasActivity.this.vncCanvas.processPointerEvent(event, true)) {
                return true;
            }
            return VncCanvasActivity.super.onTouchEvent(event);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return false;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getText(R.string.input_mode_mouse);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return "MOUSE";
        }
    }

    /* access modifiers changed from: package-private */
    public class DPadPanTouchMouseMode implements AbstractInputHandler {
        private boolean isPanning;

        DPadPanTouchMouseMode() {
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyDown(int keyCode, KeyEvent evt) {
            int xv = 0;
            int yv = 0;
            boolean result = true;
            switch (keyCode) {
                case AbstractConnectionBean.GEN_ID_SHOWZOOMBUTTONS /*{ENCODED_INT: 19}*/:
                    yv = -1;
                    break;
                case 20:
                    yv = 1;
                    break;
                case AbstractConnectionBean.GEN_ID_USEIMMERSIVE /*{ENCODED_INT: 21}*/:
                    xv = -1;
                    break;
                case AbstractConnectionBean.GEN_ID_USEWAKELOCK /*{ENCODED_INT: 22}*/:
                    xv = 1;
                    break;
                default:
                    result = VncCanvasActivity.this.defaultKeyDownHandler(keyCode, evt);
                    break;
            }
            if (!(xv == 0 && yv == 0) && !this.isPanning) {
                this.isPanning = true;
                final int finalXv = xv;
                final int finalYv = yv;
                VncCanvasActivity.this.panner.start((float) xv, (float) yv, new Panner.VelocityUpdater() {
                    /* class android.androidVNC.VncCanvasActivity.DPadPanTouchMouseMode.C00491 */

                    @Override // android.androidVNC.Panner.VelocityUpdater
                    public boolean updateVelocity(PointF p, long interval) {
                        double scale = (2.0d * ((double) interval)) / 50.0d;
                        if (Math.abs(p.x) < 500.0f) {
                            p.x += (float) ((int) (((double) finalXv) * scale));
                        }
                        if (Math.abs(p.y) >= 500.0f) {
                            return true;
                        }
                        p.y += (float) ((int) (((double) finalYv) * scale));
                        return true;
                    }
                });
                VncCanvasActivity.this.vncCanvas.pan(xv, yv);
            }
            return result;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onKeyUp(int keyCode, KeyEvent evt) {
            switch (keyCode) {
                case AbstractConnectionBean.GEN_ID_SHOWZOOMBUTTONS /*{ENCODED_INT: 19}*/:
                case 20:
                case AbstractConnectionBean.GEN_ID_USEIMMERSIVE /*{ENCODED_INT: 21}*/:
                case AbstractConnectionBean.GEN_ID_USEWAKELOCK /*{ENCODED_INT: 22}*/:
                    VncCanvasActivity.this.panner.stop();
                    this.isPanning = false;
                    return true;
                default:
                    return VncCanvasActivity.this.defaultKeyUpHandler(keyCode, evt);
            }
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTouchEvent(MotionEvent event) {
            VncCanvasActivity.this.vncCanvas.changeTouchCoordinatesToFullFrame(event);
            if (VncCanvasActivity.this.vncCanvas.processPointerEvent(event, true)) {
                return true;
            }
            return VncCanvasActivity.super.onTouchEvent(event);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public boolean onTrackballEvent(MotionEvent evt) {
            return false;
        }

        @Override // android.androidVNC.AbstractInputHandler
        public CharSequence getHandlerDescription() {
            return VncCanvasActivity.this.getResources().getText(R.string.input_mode_dpad_pan_touchpad_mouse);
        }

        @Override // android.androidVNC.AbstractInputHandler
        public String getName() {
            return "DPAD_PAN_TOUCH_MOUSE";
        }
    }
}
