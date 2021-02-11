package android.androidVNC;

import com.antlersoft.android.dbimpl.NewInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/* access modifiers changed from: package-private */
public class MetaKeyBean extends AbstractMetaKeyBean implements Comparable<MetaKeyBean> {
    static final NewInstance<MetaKeyBean> NEW = new NewInstance<MetaKeyBean>() {
        /* class android.androidVNC.MetaKeyBean.C00171 */

        @Override // com.antlersoft.android.dbimpl.NewInstance
        public MetaKeyBean get() {
            return new MetaKeyBean();
        }
    };
    static final ArrayList<MetaKeyBase> allKeys = new ArrayList<>();
    static final String[] allKeysNames = new String[allKeys.size()];
    static final HashMap<Integer, MetaKeyBase> keysByKeyCode = new HashMap<>();
    static final HashMap<Integer, MetaKeyBase> keysByKeySym = new HashMap<>();
    static final HashMap<Integer, MetaKeyBase> keysByMouseButton = new HashMap<>();
    static final MetaKeyBean keyArrowDown = new MetaKeyBean(0, 0, keysByKeySym.get(65364));
    static final MetaKeyBean keyArrowLeft = new MetaKeyBean(0, 0, keysByKeySym.get(65361));
    static final MetaKeyBean keyArrowRight = new MetaKeyBean(0, 0, keysByKeySym.get(65363));
    static final MetaKeyBean keyArrowUp = new MetaKeyBean(0, 0, keysByKeySym.get(65362));
    static final MetaKeyBean keyCtrlAltDel = new MetaKeyBean(0, 6, keysByKeyCode.get(67));
    private boolean _regenDesc;

    static {
        allKeys.add(new MetaKeyBase("Hangul", 65329));
        allKeys.add(new MetaKeyBase("Hangul_Start", 65330));
        allKeys.add(new MetaKeyBase("Hangul_End", 65331));
        allKeys.add(new MetaKeyBase("Hangul_Hanja", 65332));
        allKeys.add(new MetaKeyBase("Kana_Shift", 65326));
        allKeys.add(new MetaKeyBase("Right_Alt", 65514));
        allKeys.add(new MetaKeyBase(1, "Mouse Left"));
        allKeys.add(new MetaKeyBase(2, "Mouse Middle"));
        allKeys.add(new MetaKeyBase(4, "Mouse Right"));
        allKeys.add(new MetaKeyBase(16, "Mouse Scroll Down"));
        allKeys.add(new MetaKeyBase(8, "Mouse Scroll Up"));
        allKeys.add(new MetaKeyBase("Home", 65360));
        allKeys.add(new MetaKeyBase("Arrow Left", 65361));
        allKeys.add(new MetaKeyBase("Arrow Up", 65362));
        allKeys.add(new MetaKeyBase("Arrow Right", 65363));
        allKeys.add(new MetaKeyBase("Arrow Down", 65364));
        allKeys.add(new MetaKeyBase("Page Up", 65365));
        allKeys.add(new MetaKeyBase("Page Down", 65366));
        allKeys.add(new MetaKeyBase("End", 65367));
        allKeys.add(new MetaKeyBase("Insert", 65379));
        allKeys.add(new MetaKeyBase("Delete", 65535, 67));
        allKeys.add(new MetaKeyBase("Num Lock", 65407));
        allKeys.add(new MetaKeyBase("Break", 65387));
        allKeys.add(new MetaKeyBase("Scroll Lock", 65300));
        allKeys.add(new MetaKeyBase("Print Scrn", 65301));
        allKeys.add(new MetaKeyBase("Escape", 65307));
        allKeys.add(new MetaKeyBase("Enter", 65293, 66));
        allKeys.add(new MetaKeyBase("Tab", 65289, 61));
        allKeys.add(new MetaKeyBase("BackSpace", 65288));
        allKeys.add(new MetaKeyBase("Space", 32, 62));
        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < 26; i++) {
            sb.setCharAt(0, (char) (i + 65));
            allKeys.add(new MetaKeyBase(sb.toString(), i + 97, i + 29));
        }
        for (int i2 = 0; i2 < 10; i2++) {
            sb.setCharAt(0, (char) (i2 + 48));
            allKeys.add(new MetaKeyBase(sb.toString(), i2 + 48, i2 + 7));
        }
        for (int i3 = 0; i3 < 12; i3++) {
            sb.setLength(0);
            sb.append('F');
            if (i3 < 9) {
                sb.append(' ');
            }
            sb.append(Integer.toString(i3 + 1));
            allKeys.add(new MetaKeyBase(sb.toString(), 65470 + i3));
        }
        Collections.sort(allKeys);
        for (int i4 = 0; i4 < allKeysNames.length; i4++) {
            MetaKeyBase b = allKeys.get(i4);
            allKeysNames[i4] = b.name;
            if (b.isKeyEvent) {
                keysByKeyCode.put(Integer.valueOf(b.keyEvent), b);
            }
            if (b.isMouse) {
                keysByMouseButton.put(Integer.valueOf(b.mouseButtons), b);
            } else {
                keysByKeySym.put(Integer.valueOf(b.keySym), b);
            }
        }
    }

    MetaKeyBean() {
    }

    MetaKeyBean(MetaKeyBean toCopy) {
        this._regenDesc = true;
        if (toCopy.isMouseClick()) {
            setMouseButtons(toCopy.getMouseButtons());
        } else {
            setKeySym(toCopy.getKeySym());
        }
        setMetaListId(toCopy.getMetaListId());
        setMetaFlags(toCopy.getMetaFlags());
    }

    MetaKeyBean(long listId, int metaFlags, MetaKeyBase base) {
        setMetaListId(listId);
        setKeyBase(base);
        setMetaFlags(metaFlags);
        this._regenDesc = true;
    }

    @Override // android.androidVNC.AbstractMetaKeyBean, android.androidVNC.IMetaKey
    public String getKeyDesc() {
        MetaKeyBase base;
        if (this._regenDesc) {
            synchronized (this) {
                if (this._regenDesc) {
                    StringBuilder sb = new StringBuilder();
                    int meta = getMetaFlags();
                    if ((meta & 1) != 0) {
                        sb.append("Shift");
                    }
                    if ((meta & 4) != 0) {
                        if (sb.length() > 0) {
                            sb.append('-');
                        }
                        sb.append("Ctrl");
                    }
                    if ((meta & 2) != 0) {
                        if (sb.length() > 0) {
                            sb.append('-');
                        }
                        sb.append("Alt");
                    }
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    if (isMouseClick()) {
                        base = keysByMouseButton.get(Integer.valueOf(getMouseButtons()));
                    } else {
                        base = keysByKeySym.get(Integer.valueOf(getKeySym()));
                    }
                    sb.append(base.name);
                    setKeyDesc(sb.toString());
                }
            }
        }
        return super.getKeyDesc();
    }

    @Override // android.androidVNC.AbstractMetaKeyBean
    public void setKeyDesc(String arg_keyDesc) {
        super.setKeyDesc(arg_keyDesc);
        this._regenDesc = false;
    }

    @Override // android.androidVNC.AbstractMetaKeyBean
    public void setKeySym(int arg_keySym) {
        if (arg_keySym != getKeySym() || isMouseClick()) {
            setMouseClick(false);
            this._regenDesc = true;
            super.setKeySym(arg_keySym);
        }
    }

    @Override // android.androidVNC.AbstractMetaKeyBean
    public void setMetaFlags(int arg_metaFlags) {
        if (arg_metaFlags != getMetaFlags()) {
            this._regenDesc = true;
            super.setMetaFlags(arg_metaFlags);
        }
    }

    @Override // android.androidVNC.AbstractMetaKeyBean
    public void setMouseButtons(int arg_mouseButtons) {
        if (arg_mouseButtons != getMouseButtons() || !isMouseClick()) {
            setMouseClick(true);
            this._regenDesc = true;
            super.setMouseButtons(arg_mouseButtons);
        }
    }

    /* access modifiers changed from: package-private */
    public void setKeyBase(MetaKeyBase base) {
        if (base.isMouse) {
            setMouseButtons(base.mouseButtons);
        } else {
            setKeySym(base.keySym);
        }
    }

    public boolean equals(Object o) {
        if (o instanceof MetaKeyBean) {
            return getKeyDesc().equals(((MetaKeyBean) o).getKeyDesc());
        }
        return false;
    }

    public int compareTo(MetaKeyBean another) {
        return getKeyDesc().compareTo(another.getKeyDesc());
    }
}
