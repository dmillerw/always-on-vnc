package android.androidVNC;

import android.widget.ImageView;

/* access modifiers changed from: package-private */
public class FitToScreenScaling extends AbstractScaling {
    FitToScreenScaling() {
        super(R.id.itemFitToScreen, ImageView.ScaleType.FIT_CENTER);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public boolean isAbleToPan() {
        return false;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public boolean isValidInputMode(int mode) {
        return mode == R.id.itemInputFitToScreen;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public int getDefaultHandlerId() {
        return R.id.itemInputFitToScreen;
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public void setScaleTypeForActivity(VncCanvasActivity activity) {
        super.setScaleTypeForActivity(activity);
        VncCanvas vncCanvas = activity.vncCanvas;
        activity.vncCanvas.absoluteYPosition = 0;
        vncCanvas.absoluteXPosition = 0;
        activity.vncCanvas.scrollTo(0, 0);
    }
}
