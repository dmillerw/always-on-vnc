package android.androidVNC;

import android.widget.ImageView;

/* access modifiers changed from: package-private */
public class OneToOneScaling extends AbstractScaling {
    public OneToOneScaling() {
        super(R.id.itemOneToOne, ImageView.ScaleType.CENTER);
    }

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public int getDefaultHandlerId() {
        return R.id.itemInputTouchPanTrackballMouse;
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

    /* access modifiers changed from: package-private */
    @Override // android.androidVNC.AbstractScaling
    public void setScaleTypeForActivity(VncCanvasActivity activity) {
        super.setScaleTypeForActivity(activity);
        activity.vncCanvas.scrollToAbsolute();
        activity.vncCanvas.pan(0, 0);
    }
}
