package android.androidVNC;

import android.graphics.PointF;
import android.os.Handler;
import android.os.SystemClock;

class Panner implements Runnable {
    private static final String TAG = "PANNER";
    VncCanvasActivity activity;
    Handler handler;
    long lastSent;
    VelocityUpdater updater;
    PointF velocity = new PointF();

    /* access modifiers changed from: package-private */
    public interface VelocityUpdater {
        boolean updateVelocity(PointF pointF, long j);
    }

    /* access modifiers changed from: package-private */
    public static class DefaultUpdater implements VelocityUpdater {
        static DefaultUpdater instance = new DefaultUpdater();

        DefaultUpdater() {
        }

        @Override // android.androidVNC.Panner.VelocityUpdater
        public boolean updateVelocity(PointF p, long interval) {
            return true;
        }
    }

    Panner(VncCanvasActivity act, Handler hand) {
        this.activity = act;
        this.handler = hand;
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        this.handler.removeCallbacks(this);
    }

    /* access modifiers changed from: package-private */
    public void start(float xv, float yv, VelocityUpdater update) {
        if (update == null) {
            update = DefaultUpdater.instance;
        }
        this.updater = update;
        this.velocity.x = xv;
        this.velocity.y = yv;
        this.lastSent = SystemClock.uptimeMillis();
        this.handler.postDelayed(this, 50);
    }

    public void run() {
        long interval = SystemClock.uptimeMillis() - this.lastSent;
        this.lastSent += interval;
        double scale = ((double) interval) / 50.0d;
        if (!this.activity.vncCanvas.pan((int) (((double) this.velocity.x) * scale), (int) (((double) this.velocity.y) * scale))) {
            stop();
        } else if (this.updater.updateVelocity(this.velocity, interval)) {
            this.handler.postDelayed(this, 50);
        } else {
            stop();
        }
    }
}
