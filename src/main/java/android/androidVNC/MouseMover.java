package android.androidVNC;

import android.os.Handler;
import android.os.SystemClock;

/* access modifiers changed from: package-private */
public class MouseMover extends Panner {
    public MouseMover(VncCanvasActivity act, Handler hand) {
        super(act, hand);
    }

    @Override // android.androidVNC.Panner
    public void run() {
        long interval = SystemClock.uptimeMillis() - this.lastSent;
        this.lastSent += interval;
        double scale = ((double) interval) / 50.0d;
        VncCanvas canvas = this.activity.vncCanvas;
        if (!canvas.processPointerEvent((int) (((double) canvas.mouseX) + (((double) this.velocity.x) * scale)), (int) (((double) canvas.mouseY) + (((double) this.velocity.y) * scale)), 2, 0, false, false)) {
            stop();
        } else if (this.updater.updateVelocity(this.velocity, interval)) {
            this.handler.postDelayed(this, 50);
        } else {
            stop();
        }
    }
}
