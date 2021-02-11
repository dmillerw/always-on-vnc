package android.androidVNC;

import android.androidVNC.Panner;
import android.graphics.PointF;
import android.os.Handler;
import android.view.KeyEvent;

class DPadMouseKeyHandler {
    private VncCanvasActivity activity;
    private VncCanvas canvas;
    private boolean isMoving;
    private boolean mouseDown;
    private MouseMover mouseMover;

    DPadMouseKeyHandler(VncCanvasActivity activity2, Handler handler) {
        this.activity = activity2;
        this.canvas = activity2.vncCanvas;
        this.mouseMover = new MouseMover(activity2, handler);
    }

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
            case AbstractConnectionBean.GEN_COUNT /*{ENCODED_INT: 23}*/:
                if (!this.mouseDown) {
                    this.mouseDown = true;
                    result = this.canvas.processPointerEvent(this.canvas.mouseX, this.canvas.mouseY, 0, evt.getMetaState(), this.mouseDown, this.canvas.cameraButtonDown);
                    break;
                }
                break;
            default:
                result = this.activity.defaultKeyDownHandler(keyCode, evt);
                break;
        }
        if (!(xv == 0 && yv == 0) && !this.isMoving) {
            this.isMoving = true;
            final int finalXv = xv;
            final int finalYv = yv;
            this.mouseMover.start((float) xv, (float) yv, new Panner.VelocityUpdater() {
                /* class android.androidVNC.DPadMouseKeyHandler.C00031 */

                @Override // android.androidVNC.Panner.VelocityUpdater
                public boolean updateVelocity(PointF p, long interval) {
                    double scale = (1.2d * ((double) interval)) / 50.0d;
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
            this.canvas.processPointerEvent(this.canvas.mouseX + xv, this.canvas.mouseY + yv, 2, evt.getMetaState(), this.mouseDown, this.canvas.cameraButtonDown);
        }
        return result;
    }

    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch (keyCode) {
            case AbstractConnectionBean.GEN_ID_SHOWZOOMBUTTONS /*{ENCODED_INT: 19}*/:
            case 20:
            case AbstractConnectionBean.GEN_ID_USEIMMERSIVE /*{ENCODED_INT: 21}*/:
            case AbstractConnectionBean.GEN_ID_USEWAKELOCK /*{ENCODED_INT: 22}*/:
                this.mouseMover.stop();
                this.isMoving = false;
                return true;
            case AbstractConnectionBean.GEN_COUNT /*{ENCODED_INT: 23}*/:
                if (!this.mouseDown) {
                    return true;
                }
                this.mouseDown = false;
                return this.canvas.processPointerEvent(this.canvas.mouseX, this.canvas.mouseY, 1, evt.getMetaState(), this.mouseDown, this.canvas.cameraButtonDown);
            default:
                return this.activity.defaultKeyUpHandler(keyCode, evt);
        }
    }
}
