package android.androidVNC;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import com.antlersoft.android.drawing.OverlappingCopy;
import com.antlersoft.android.drawing.RectList;
import com.antlersoft.util.ObjectPool;
import java.io.IOException;

/* access modifiers changed from: package-private */
public class LargeBitmapData extends AbstractBitmapData {
    static final int CAPACITY_MULTIPLIER = 21;
    private static ObjectPool<Rect> rectPool = new ObjectPool<Rect>() {
        /* class android.androidVNC.LargeBitmapData.C00161 */

        /* access modifiers changed from: protected */
        @Override // com.antlersoft.util.ObjectPool
        public Rect itemForPool() {
            return new Rect();
        }
    };
    private Rect bitmapRect;
    private Paint defaultPaint;
    private RectList invalidList;
    private RectList pendingList;
    int scrolledToX;
    int scrolledToY;
    int xoffset;
    int yoffset;

    class LargeBitmapDrawable extends AbstractBitmapDrawable {
        LargeBitmapDrawable() {
            super(LargeBitmapData.this);
        }

        public void draw(Canvas canvas) {
            int xoff;
            int yoff;
            synchronized (LargeBitmapData.this) {
                xoff = LargeBitmapData.this.xoffset;
                yoff = LargeBitmapData.this.yoffset;
            }
            draw(canvas, xoff, yoff);
        }
    }

    LargeBitmapData(RfbProto p, VncCanvas c, int displayWidth, int displayHeight, int capacity) {
        super(p, c);
        double scaleMultiplier = Math.sqrt(((double) ((capacity * 1024) * 1024)) / ((double) ((this.framebufferwidth * 21) * this.framebufferheight)));
        scaleMultiplier = scaleMultiplier > 1.0d ? 1.0d : scaleMultiplier;
        this.bitmapwidth = (int) (((double) this.framebufferwidth) * scaleMultiplier);
        if (this.bitmapwidth < displayWidth) {
            this.bitmapwidth = displayWidth;
        }
        this.bitmapheight = (int) (((double) this.framebufferheight) * scaleMultiplier);
        if (this.bitmapheight < displayHeight) {
            this.bitmapheight = displayHeight;
        }
        Log.i("LBM", "bitmapsize = (" + this.bitmapwidth + "," + this.bitmapheight + ")");
        this.mbitmap = Bitmap.createBitmap(this.bitmapwidth, this.bitmapheight, Bitmap.Config.RGB_565);
        this.memGraphics = new Canvas(this.mbitmap);
        this.bitmapPixels = new int[(this.bitmapwidth * this.bitmapheight)];
        this.invalidList = new RectList(rectPool);
        this.pendingList = new RectList(rectPool);
        this.bitmapRect = new Rect(0, 0, this.bitmapwidth, this.bitmapheight);
        this.defaultPaint = new Paint();
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public AbstractBitmapDrawable createDrawable() {
        return new LargeBitmapDrawable();
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void copyRect(Rect src, Rect dest, Paint paint) {
        throw new RuntimeException("copyrect Not implemented");
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void drawRect(int x, int y, int w, int h, Paint paint) {
        int x2 = x - this.xoffset;
        int y2 = y - this.yoffset;
        this.memGraphics.drawRect((float) x2, (float) y2, (float) (x2 + w), (float) (y2 + h), paint);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public int offset(int x, int y) {
        return (((y - this.yoffset) * this.bitmapwidth) + x) - this.xoffset;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public synchronized void scrollChanged(int newx, int newy) {
        int newScrolledToX = this.scrolledToX;
        int newScrolledToY = this.scrolledToY;
        int visibleWidth = this.vncCanvas.getVisibleWidth();
        int visibleHeight = this.vncCanvas.getVisibleHeight();
        if (newx - this.xoffset < 0) {
            newScrolledToX = ((visibleWidth / 2) + newx) - (this.bitmapwidth / 2);
            if (newScrolledToX < 0) {
                newScrolledToX = 0;
            }
        } else if ((newx - this.xoffset) + visibleWidth > this.bitmapwidth) {
            newScrolledToX = ((visibleWidth / 2) + newx) - (this.bitmapwidth / 2);
            if (this.bitmapwidth + newScrolledToX > this.framebufferwidth) {
                newScrolledToX = this.framebufferwidth - this.bitmapwidth;
            }
        }
        if (newy - this.yoffset < 0) {
            newScrolledToY = ((visibleHeight / 2) + newy) - (this.bitmapheight / 2);
            if (newScrolledToY < 0) {
                newScrolledToY = 0;
            }
        } else if ((newy - this.yoffset) + visibleHeight > this.bitmapheight) {
            newScrolledToY = ((visibleHeight / 2) + newy) - (this.bitmapheight / 2);
            if (this.bitmapheight + newScrolledToY > this.framebufferheight) {
                newScrolledToY = this.framebufferheight - this.bitmapheight;
            }
        }
        if (!(newScrolledToX == this.scrolledToX && newScrolledToY == this.scrolledToY)) {
            this.scrolledToX = newScrolledToX;
            this.scrolledToY = newScrolledToY;
            if (this.waitingForInput) {
                syncScroll();
            }
        }
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public void updateBitmap(int x, int y, int w, int h) {
        this.mbitmap.setPixels(this.bitmapPixels, offset(x, y), this.bitmapwidth, x - this.xoffset, y - this.yoffset, w, h);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public synchronized boolean validDraw(int x, int y, int w, int h) {
        boolean result;
        result = x - this.xoffset >= 0 && (x - this.xoffset) + w <= this.bitmapwidth && y - this.yoffset >= 0 && (y - this.yoffset) + h <= this.bitmapheight;
        ObjectPool.Entry<Rect> entry = rectPool.reserve();
        Rect r = entry.get();
        r.set(x, y, x + w, y + h);
        this.pendingList.subtract(r);
        if (!result) {
            this.invalidList.add(r);
        } else {
            this.invalidList.subtract(r);
        }
        rectPool.release(entry);
        return result;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public synchronized void writeFullUpdateRequest(boolean incremental) throws IOException {
        if (!incremental) {
            ObjectPool.Entry<Rect> entry = rectPool.reserve();
            Rect r = entry.get();
            r.left = this.xoffset;
            r.top = this.yoffset;
            r.right = this.xoffset + this.bitmapwidth;
            r.bottom = this.yoffset + this.bitmapheight;
            this.pendingList.add(r);
            this.invalidList.add(r);
            rectPool.release(entry);
        }
        this.rfb.writeFramebufferUpdateRequest(this.xoffset, this.yoffset, this.bitmapwidth, this.bitmapheight, incremental);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractBitmapData
    public synchronized void syncScroll() {
        int deltaX = this.xoffset - this.scrolledToX;
        int deltaY = this.yoffset - this.scrolledToY;
        this.xoffset = this.scrolledToX;
        this.yoffset = this.scrolledToY;
        this.bitmapRect.top = this.scrolledToY;
        this.bitmapRect.bottom = this.scrolledToY + this.bitmapheight;
        this.bitmapRect.left = this.scrolledToX;
        this.bitmapRect.right = this.scrolledToX + this.bitmapwidth;
        this.invalidList.intersect(this.bitmapRect);
        if (!(deltaX == 0 && deltaY == 0)) {
            boolean didOverlapping = false;
            if (Math.abs(deltaX) < this.bitmapwidth && Math.abs(deltaY) < this.bitmapheight) {
                ObjectPool.Entry<Rect> sourceEntry = rectPool.reserve();
                ObjectPool.Entry<Rect> addedEntry = rectPool.reserve();
                try {
                    Rect added = addedEntry.get();
                    Rect sourceRect = sourceEntry.get();
                    sourceRect.set(deltaX < 0 ? -deltaX : 0, deltaY < 0 ? -deltaY : 0, deltaX < 0 ? this.bitmapwidth : this.bitmapwidth - deltaX, deltaY < 0 ? this.bitmapheight : this.bitmapheight - deltaY);
                    if (!this.invalidList.testIntersect(sourceRect)) {
                        didOverlapping = true;
                        OverlappingCopy.Copy(this.mbitmap, this.memGraphics, this.defaultPaint, sourceRect, sourceRect.left + deltaX, sourceRect.top + deltaY, rectPool);
                        if (deltaX != 0) {
                            added.left = deltaX < 0 ? this.bitmapRect.right + deltaX : this.bitmapRect.left;
                            added.right = added.left + Math.abs(deltaX);
                            added.top = this.bitmapRect.top;
                            added.bottom = this.bitmapRect.bottom;
                            this.invalidList.add(added);
                        }
                        if (deltaY != 0) {
                            added.left = deltaX < 0 ? this.bitmapRect.left : this.bitmapRect.left + deltaX;
                            added.top = deltaY < 0 ? this.bitmapRect.bottom + deltaY : this.bitmapRect.top;
                            added.right = (added.left + this.bitmapwidth) - Math.abs(deltaX);
                            added.bottom = added.top + Math.abs(deltaY);
                            this.invalidList.add(added);
                        }
                    }
                } finally {
                    rectPool.release(addedEntry);
                    rectPool.release(sourceEntry);
                }
            }
            if (!didOverlapping) {
                try {
                    this.mbitmap.eraseColor(-16711936);
                    writeFullUpdateRequest(false);
                } catch (IOException e) {
                }
            }
        }
        int size = this.pendingList.getSize();
        for (int i = 0; i < size; i++) {
            this.invalidList.subtract(this.pendingList.get(i));
        }
        int size2 = this.invalidList.getSize();
        for (int i2 = 0; i2 < size2; i2++) {
            Rect invalidRect = this.invalidList.get(i2);
            try {
                this.rfb.writeFramebufferUpdateRequest(invalidRect.left, invalidRect.top, invalidRect.right - invalidRect.left, invalidRect.bottom - invalidRect.top, false);
                this.pendingList.add(invalidRect);
            } catch (IOException e2) {
            }
        }
        this.waitingForInput = true;
    }
}
