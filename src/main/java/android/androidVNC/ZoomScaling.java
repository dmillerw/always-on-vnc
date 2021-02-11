package android.androidVNC;

import android.graphics.Matrix;
import android.widget.ImageView;

/* access modifiers changed from: package-private */
public class ZoomScaling extends AbstractScaling {
    static final String TAG = "ZoomScaling";
    int canvasXOffset;
    int canvasYOffset;
    private Matrix matrix = new Matrix();
    float minimumScale;
    float scaling = 1.0f;

    public ZoomScaling() {
        super(R.id.itemZoomable, ImageView.ScaleType.MATRIX);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public int getDefaultHandlerId() {
        return R.id.itemInputTouchPanZoomMouse;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public boolean isAbleToPan() {
        return true;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public boolean isValidInputMode(int mode) {
        return mode != R.id.itemInputFitToScreen;
    }

    private void resolveZoom(VncCanvasActivity activity) {
        activity.vncCanvas.scrollToAbsolute();
        activity.vncCanvas.pan(0, 0);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public void zoomIn(VncCanvasActivity activity) {
        resetMatrix();
        standardizeScaling();
        this.scaling = (float) (((double) this.scaling) + 0.25d);
        if (((double) this.scaling) > 4.0d) {
            this.scaling = 4.0f;
            activity.zoomer.setIsZoomInEnabled(false);
        }
        activity.zoomer.setIsZoomOutEnabled(true);
        this.matrix.postScale(this.scaling, this.scaling);
        activity.vncCanvas.setImageMatrix(this.matrix);
        resolveZoom(activity);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public float getScale() {
        return this.scaling;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public void zoomOut(VncCanvasActivity activity) {
        resetMatrix();
        standardizeScaling();
        this.scaling = (float) (((double) this.scaling) - 0.25d);
        if (this.scaling < this.minimumScale) {
            this.scaling = this.minimumScale;
            activity.zoomer.setIsZoomOutEnabled(false);
        }
        activity.zoomer.setIsZoomInEnabled(true);
        this.matrix.postScale(this.scaling, this.scaling);
        activity.vncCanvas.setImageMatrix(this.matrix);
        resolveZoom(activity);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public void adjust(VncCanvasActivity activity, float scaleFactor, float fx, float fy) {
        float newScale = scaleFactor * this.scaling;
        if (scaleFactor < 1.0f) {
            if (newScale < this.minimumScale) {
                newScale = this.minimumScale;
                activity.zoomer.setIsZoomOutEnabled(false);
            }
            activity.zoomer.setIsZoomInEnabled(true);
        } else {
            if (newScale > 4.0f) {
                newScale = 4.0f;
                activity.zoomer.setIsZoomInEnabled(false);
            }
            activity.zoomer.setIsZoomOutEnabled(true);
        }
        int xPan = activity.vncCanvas.absoluteXPosition;
        float ax = (fx / this.scaling) + ((float) xPan);
        float newXPan = (((this.scaling * ((float) xPan)) - (this.scaling * ax)) + (newScale * ax)) / newScale;
        int yPan = activity.vncCanvas.absoluteYPosition;
        float ay = (fy / this.scaling) + ((float) yPan);
        float newYPan = (((this.scaling * ((float) yPan)) - (this.scaling * ay)) + (newScale * ay)) / newScale;
        resetMatrix();
        this.scaling = newScale;
        this.matrix.postScale(this.scaling, this.scaling);
        activity.vncCanvas.setImageMatrix(this.matrix);
        resolveZoom(activity);
        activity.vncCanvas.pan((int) (newXPan - ((float) xPan)), (int) (newYPan - ((float) yPan)));
    }

    private void resetMatrix() {
        this.matrix.reset();
        this.matrix.preTranslate((float) this.canvasXOffset, (float) this.canvasYOffset);
    }

    private void standardizeScaling() {
        this.scaling = ((float) ((int) (this.scaling * 4.0f))) / 4.0f;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public void setScaleTypeForActivity(VncCanvasActivity activity) {
        super.setScaleTypeForActivity(activity);
        this.scaling = 1.0f;
        this.minimumScale = activity.vncCanvas.bitmapData.getMinimumScale();
        this.canvasXOffset = -activity.vncCanvas.getCenteredXOffset();
        this.canvasYOffset = -activity.vncCanvas.getCenteredYOffset();
        resetMatrix();
        activity.vncCanvas.setImageMatrix(this.matrix);
        resolveZoom(activity);
    }
}
