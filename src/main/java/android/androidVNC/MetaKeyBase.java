package android.androidVNC;

/* access modifiers changed from: package-private */
public class MetaKeyBase implements Comparable<MetaKeyBase> {
    boolean isKeyEvent = false;
    boolean isMouse = true;
    int keyEvent;
    int keySym;
    int mouseButtons;
    String name;

    MetaKeyBase(int mouseButtons2, String name2) {
        this.mouseButtons = mouseButtons2;
        this.name = name2;
    }

    MetaKeyBase(String name2, int keySym2, int keyEvent2) {
        this.name = name2;
        this.keySym = keySym2;
        this.keyEvent = keyEvent2;
    }

    MetaKeyBase(String name2, int keySym2) {
        this.name = name2;
        this.keySym = keySym2;
    }

    public int compareTo(MetaKeyBase another) {
        return this.name.compareTo(another.name);
    }
}
