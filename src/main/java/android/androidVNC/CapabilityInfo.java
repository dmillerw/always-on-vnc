package android.androidVNC;

/* access modifiers changed from: package-private */
public class CapabilityInfo {
    protected int code;
    protected String description;
    protected boolean enabled;
    protected String nameSignature;
    protected String vendorSignature;

    public CapabilityInfo(int code2, String vendorSignature2, String nameSignature2, String description2) {
        this.code = code2;
        this.vendorSignature = vendorSignature2;
        this.nameSignature = nameSignature2;
        this.description = description2;
        this.enabled = false;
    }

    public CapabilityInfo(int code2, byte[] vendorSignature2, byte[] nameSignature2) {
        this.code = code2;
        this.vendorSignature = new String(vendorSignature2);
        this.nameSignature = new String(nameSignature2);
        this.description = null;
        this.enabled = false;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public boolean equals(CapabilityInfo other) {
        return other != null && this.code == other.code && this.vendorSignature.equals(other.vendorSignature) && this.nameSignature.equals(other.nameSignature);
    }

    public boolean enableIfEquals(CapabilityInfo other) {
        if (equals(other)) {
            enable();
        }
        return isEnabled();
    }
}
