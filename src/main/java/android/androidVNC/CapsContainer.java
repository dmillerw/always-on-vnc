package android.androidVNC;

import java.util.Hashtable;
import java.util.Vector;

/* access modifiers changed from: package-private */
public class CapsContainer {
    protected Hashtable<Integer, CapabilityInfo> infoMap = new Hashtable<>(64, 0.25f);
    protected Vector<Integer> orderedList = new Vector<>(32, 8);

    public void add(CapabilityInfo capinfo) {
        this.infoMap.put(new Integer(capinfo.getCode()), capinfo);
    }

    public void add(int code, String vendor, String name, String desc) {
        this.infoMap.put(new Integer(code), new CapabilityInfo(code, vendor, name, desc));
    }

    public boolean isKnown(int code) {
        return this.infoMap.containsKey(new Integer(code));
    }

    public CapabilityInfo getInfo(int code) {
        return this.infoMap.get(new Integer(code));
    }

    public String getDescription(int code) {
        CapabilityInfo capinfo = this.infoMap.get(new Integer(code));
        if (capinfo == null) {
            return null;
        }
        return capinfo.getDescription();
    }

    public boolean enable(CapabilityInfo other) {
        Integer key = new Integer(other.getCode());
        CapabilityInfo capinfo = this.infoMap.get(key);
        if (capinfo == null) {
            return false;
        }
        boolean enabled = capinfo.enableIfEquals(other);
        if (!enabled) {
            return enabled;
        }
        this.orderedList.addElement(key);
        return enabled;
    }

    public boolean isEnabled(int code) {
        CapabilityInfo capinfo = this.infoMap.get(new Integer(code));
        if (capinfo == null) {
            return false;
        }
        return capinfo.isEnabled();
    }

    public int numEnabled() {
        return this.orderedList.size();
    }

    public int getByOrder(int idx) {
        try {
            return this.orderedList.elementAt(idx).intValue();
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
