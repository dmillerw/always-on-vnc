package android.androidVNC;

import android.widget.ImageView;

/* access modifiers changed from: package-private */
public abstract class AbstractScaling {
    private static final int[] scaleModeIds = {R.id.itemFitToScreen, R.id.itemOneToOne, R.id.itemZoomable};
    private static AbstractScaling[] scalings;

    /* renamed from: id */
    private int f0id;
    protected ImageView.ScaleType scaleType;

    /* access modifiers changed from: package-private */
    public abstract int getDefaultHandlerId();

    /* access modifiers changed from: package-private */
    public abstract boolean isAbleToPan();

    /* access modifiers changed from: package-private */
    public abstract boolean isValidInputMode(int i);

    static AbstractScaling getById(int id) {
        if (scalings == null) {
            scalings = new AbstractScaling[scaleModeIds.length];
        }
        for (int i = 0; i < scaleModeIds.length; i++) {
            if (scaleModeIds[i] == id) {
                if (scalings[i] == null) {
                    switch (id) {
                        case R.id.itemZoomable:
                            scalings[i] = new ZoomScaling();
                            break;
                        case R.id.itemOneToOne:
                            scalings[i] = new OneToOneScaling();
                            break;
                        case R.id.itemFitToScreen:
                            scalings[i] = new FitToScreenScaling();
                            break;
                    }
                }
                return scalings[i];
            }
        }
        throw new IllegalArgumentException("Unknown scaling id " + id);
    }

    /* access modifiers changed from: package-private */
    public float getScale() {
        return 1.0f;
    }

    /* access modifiers changed from: package-private */
    public void zoomIn(VncCanvasActivity activity) {
    }

    /* access modifiers changed from: package-private */
    public void zoomOut(VncCanvasActivity activity) {
    }

    static AbstractScaling getByScaleType(ImageView.ScaleType scaleType2) {
        for (int i : scaleModeIds) {
            AbstractScaling s = getById(i);
            if (s.scaleType == scaleType2) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unsupported scale type: " + scaleType2.toString());
    }

    protected AbstractScaling(int id, ImageView.ScaleType scaleType2) {
        this.f0id = id;
        this.scaleType = scaleType2;
    }

    /* access modifiers changed from: package-private */
    public int getId() {
        return this.f0id;
    }

    /* access modifiers changed from: package-private */
    public void setScaleTypeForActivity(VncCanvasActivity activity) {
        activity.zoomer.hide();
        activity.vncCanvas.scaling = this;
        activity.vncCanvas.setScaleType(this.scaleType);
        activity.getConnection().setScaleMode(this.scaleType);
        if (activity.inputHandler == null || !isValidInputMode(activity.getModeIdFromHandler(activity.inputHandler))) {
            activity.inputHandler = activity.getInputHandlerById(getDefaultHandlerId());
            activity.getConnection().setInputMode(activity.inputHandler.getName());
        }
        activity.getConnection().Gen_update(activity.database.getWritableDatabase());
        activity.updateInputMenu();
    }

    /* access modifiers changed from: package-private */
    public void adjust(VncCanvasActivity activity, float scaleFactor, float fx, float fy) {
    }
}
