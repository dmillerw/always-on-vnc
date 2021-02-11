package android.androidVNC;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.io.IOException;

class CompactBitmapData extends AbstractBitmapData {

    class CompactBitmapDrawable extends AbstractBitmapDrawable {
        CompactBitmapDrawable() {
            super(CompactBitmapData.this);
        }

        public void draw(Canvas canvas) {
            draw(canvas, 0, 0);
        }
    }

    CompactBitmapData(RfbProto rfb, VncCanvas c) {
        super(rfb, c);
        this.bitmapwidth = this.framebufferwidth;
        this.bitmapheight = this.framebufferheight;
        this.mbitmap = Bitmap.createBitmap(rfb.framebufferWidth, rfb.framebufferHeight, Bitmap.Config.RGB_565);
        this.memGraphics = new Canvas(this.mbitmap);
        this.bitmapPixels = new int[(rfb.framebufferWidth * rfb.framebufferHeight)];
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void writeFullUpdateRequest(boolean incremental) throws IOException {
        this.rfb.writeFramebufferUpdateRequest(0, 0, this.framebufferwidth, this.framebufferheight, incremental);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public boolean validDraw(int x, int y, int w, int h) {
        return true;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public int offset(int x, int y) {
        return (this.bitmapwidth * y) + x;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public AbstractBitmapDrawable createDrawable() {
        return new CompactBitmapDrawable();
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void updateBitmap(int x, int y, int w, int h) {
        this.mbitmap.setPixels(this.bitmapPixels, offset(x, y), this.bitmapwidth, x, y, w, h);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void copyRect(Rect src, Rect dest, Paint paint) {
        this.memGraphics.drawBitmap(this.mbitmap, src, dest, paint);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void drawRect(int x, int y, int w, int h, Paint paint) {
        this.memGraphics.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), paint);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void scrollChanged(int newx, int newy) {
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void syncScroll() {
    }
}
