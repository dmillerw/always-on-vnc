package android.androidVNC;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

import java.io.IOException;

/* access modifiers changed from: package-private */
public abstract class AbstractBitmapData {
    int[] bitmapPixels;
    int bitmapheight;
    int bitmapwidth;
    private AbstractBitmapDrawable drawable;
    int framebufferheight;
    int framebufferwidth;
    Bitmap mbitmap;
    Canvas memGraphics;
    RfbProto rfb;
    VncCanvas vncCanvas;
    boolean waitingForInput;

    /* access modifiers changed from: package-private */
    public abstract void copyRect(Rect rect, Rect rect2, Paint paint);

    /* access modifiers changed from: package-private */
    public abstract AbstractBitmapDrawable createDrawable();

    /* access modifiers changed from: package-private */
    public abstract void drawRect(int i, int i2, int i3, int i4, Paint paint);

    /* access modifiers changed from: package-private */
    public abstract int offset(int i, int i2);

    /* access modifiers changed from: package-private */
    public abstract void scrollChanged(int i, int i2);

    /* access modifiers changed from: package-private */
    public abstract void syncScroll();

    /* access modifiers changed from: package-private */
    public abstract void updateBitmap(int i, int i2, int i3, int i4);

    /* access modifiers changed from: package-private */
    public abstract boolean validDraw(int i, int i2, int i3, int i4);

    /* access modifiers changed from: package-private */
    public abstract void writeFullUpdateRequest(boolean z) throws IOException;

    AbstractBitmapData(RfbProto p, VncCanvas c) {
        this.rfb = p;
        this.vncCanvas = c;

        this.framebufferheight = this.rfb.framebufferHeight;
        this.framebufferwidth = this.rfb.framebufferWidth;
    }

    /* access modifiers changed from: package-private */
    public synchronized void doneWaiting() {
        this.waitingForInput = false;
    }

    /* access modifiers changed from: package-private */
    public final void invalidateMousePosition() {
        if (this.vncCanvas.connection.getUseLocalCursor()) {
            if (this.drawable == null) {
                this.drawable = createDrawable();
            }
            this.drawable.setCursorRect(this.vncCanvas.mouseX, this.vncCanvas.mouseY);
            this.vncCanvas.invalidate(this.drawable.cursorRect);
        }
    }

    /* access modifiers changed from: package-private */
    public float getMinimumScale() {
        double scale = 0.75d;
        int displayWidth = this.vncCanvas.getWidth();
        int displayHeight = this.vncCanvas.getHeight();
        while (scale >= 0.0d && ((double) this.bitmapwidth) * scale >= ((double) displayWidth) && ((double) this.bitmapheight) * scale >= ((double) displayHeight)) {
            scale -= 0.25d;
        }
        return (float) (scale + 0.25d);
    }

    /* access modifiers changed from: package-private */
    public void updateView(ImageView v) {
        if (this.drawable == null) {
            this.drawable = createDrawable();
        }
        v.setImageDrawable(this.drawable);
        v.invalidate();
    }

    /* access modifiers changed from: package-private */
    public void dispose() {
        if (this.mbitmap != null) {
            this.mbitmap.recycle();
        }
        this.memGraphics = null;
        this.bitmapPixels = null;
    }
}
