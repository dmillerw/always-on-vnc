package android.androidVNC;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.DrawableContainer;

public class AbstractBitmapDrawable extends DrawableContainer {
    static final Paint _blackPaint = new Paint();
    static final Paint _defaultPaint = new Paint();
    static final Paint _whitePaint = new Paint();
    Rect clipRect = new Rect();
    Rect cursorRect = new Rect();
    AbstractBitmapData data;

    static {
        _whitePaint.setColor(-1);
        _blackPaint.setColor(-16777216);
    }

    AbstractBitmapDrawable(AbstractBitmapData data2) {
        this.data = data2;
    }

    /* access modifiers changed from: package-private */
    public void draw(Canvas canvas, int xoff, int yoff) {
        canvas.drawBitmap(this.data.mbitmap, (float) xoff, (float) yoff, _defaultPaint);
        if (this.data.vncCanvas.connection.getUseLocalCursor()) {
            setCursorRect(this.data.vncCanvas.mouseX, this.data.vncCanvas.mouseY);
            this.clipRect.set(this.cursorRect);
            if (canvas.clipRect(this.cursorRect)) {
                drawCursor(canvas);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void drawCursor(Canvas canvas) {
        canvas.drawRect(this.cursorRect, _whitePaint);
        canvas.drawRect(((float) this.cursorRect.left) + 1.0f, ((float) this.cursorRect.top) + 1.0f, ((float) this.cursorRect.right) - 1.0f, ((float) this.cursorRect.bottom) - 1.0f, _blackPaint);
    }

    /* access modifiers changed from: package-private */
    public void setCursorRect(int mouseX, int mouseY) {
        this.cursorRect.left = mouseX - 2;
        this.cursorRect.right = this.cursorRect.left + 4;
        this.cursorRect.top = mouseY - 2;
        this.cursorRect.bottom = this.cursorRect.top + 4;
    }

    public int getIntrinsicHeight() {
        return this.data.framebufferheight;
    }

    public int getIntrinsicWidth() {
        return this.data.framebufferwidth;
    }

    public int getOpacity() {
        return -1;
    }

    public boolean isStateful() {
        return false;
    }
}
