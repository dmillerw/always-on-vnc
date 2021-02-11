package android.androidVNC;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.util.Arrays;

/* access modifiers changed from: package-private */
public class FullBufferBitmapData extends AbstractBitmapData {
    static final int CAPACITY_MULTIPLIER = 7;
    int xoffset;
    int yoffset;

    class Drawable extends AbstractBitmapDrawable {
        public Drawable(AbstractBitmapData data) {
            super(data);
        }

        public void draw(Canvas canvas) {
            if (FullBufferBitmapData.this.vncCanvas.getScaleType() == ImageView.ScaleType.FIT_CENTER) {
                canvas.drawBitmap(this.data.bitmapPixels, 0, this.data.framebufferwidth, FullBufferBitmapData.this.xoffset, FullBufferBitmapData.this.yoffset, FullBufferBitmapData.this.framebufferwidth, FullBufferBitmapData.this.framebufferheight, false, (Paint) null);
            } else {
                FullBufferBitmapData.this.vncCanvas.getScale();
                int xo = FullBufferBitmapData.this.xoffset < 0 ? 0 : FullBufferBitmapData.this.xoffset;
                int yo = FullBufferBitmapData.this.yoffset < 0 ? 0 : FullBufferBitmapData.this.yoffset;
                int drawWidth = FullBufferBitmapData.this.vncCanvas.getVisibleWidth();
                if (drawWidth + xo > this.data.framebufferwidth) {
                    drawWidth = this.data.framebufferwidth - xo;
                }
                int drawHeight = FullBufferBitmapData.this.vncCanvas.getVisibleHeight();
                if (drawHeight + yo > this.data.framebufferheight) {
                    drawHeight = this.data.framebufferheight - yo;
                }
                canvas.drawBitmap(this.data.bitmapPixels, FullBufferBitmapData.this.offset(xo, yo), this.data.framebufferwidth, xo, yo, drawWidth, drawHeight, false, (Paint) null);
            }
            if (this.data.vncCanvas.connection.getUseLocalCursor()) {
                setCursorRect(this.data.vncCanvas.mouseX, this.data.vncCanvas.mouseY);
                this.clipRect.set(this.cursorRect);
                if (canvas.clipRect(this.cursorRect)) {
                    drawCursor(canvas);
                }
            }
        }
    }

    public FullBufferBitmapData(RfbProto p, VncCanvas c, int capacity) {
        super(p, c);
        this.framebufferwidth = this.rfb.framebufferWidth;
        this.framebufferheight = this.rfb.framebufferHeight;
        this.bitmapwidth = this.framebufferwidth;
        this.bitmapheight = this.framebufferheight;
        Log.i("FBBM", "bitmapsize = (" + this.bitmapwidth + "," + this.bitmapheight + ")");
        this.bitmapPixels = new int[(this.framebufferwidth * this.framebufferheight)];
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void copyRect(Rect src, Rect dest, Paint paint) {
        throw new RuntimeException("copyrect Not implemented");
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public AbstractBitmapDrawable createDrawable() {
        return new Drawable(this);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void drawRect(int x, int y, int w, int h, Paint paint) {
        int color = paint.getColor();
        int offset = offset(x, y);
        if (w > 10) {
            int j = 0;
            while (j < h) {
                Arrays.fill(this.bitmapPixels, offset, offset + w, color);
                j++;
                offset += this.framebufferwidth;
            }
            return;
        }
        int j2 = 0;
        while (j2 < h) {
            int k = 0;
            while (k < w) {
                this.bitmapPixels[offset] = color;
                k++;
                offset++;
            }
            j2++;
            offset += this.framebufferwidth - w;
        }
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public int offset(int x, int y) {
        return (this.framebufferwidth * y) + x;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void scrollChanged(int newx, int newy) {
        this.xoffset = newx;
        this.yoffset = newy;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void syncScroll() {
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void updateBitmap(int x, int y, int w, int h) {
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public boolean validDraw(int x, int y, int w, int h) {
        return true;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void writeFullUpdateRequest(boolean incremental) throws IOException {
        this.rfb.writeFramebufferUpdateRequest(0, 0, this.framebufferwidth, this.framebufferheight, incremental);
    }
}
