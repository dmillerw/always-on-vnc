package com.antlersoft.android.drawing;

import android.graphics.Rect;
import com.antlersoft.util.ObjectPool;
import java.util.ArrayList;

public class RectList {
    static final int BOTTOM = 64;
    static final int BOTTOM_LEFT = 128;
    static final int BOTTOM_RIGHT = 32;
    static final int LEFT = 1;
    static final int RIGHT = 16;
    static final int TOP = 4;
    static final int TOP_LEFT = 2;
    static final int TOP_RIGHT = 8;
    private ArrayList<ObjectPool.Entry<Rect>> list;
    private ObjectPool<ArrayList<ObjectPool.Entry<Rect>>> listRectsPool = new ObjectPool<ArrayList<ObjectPool.Entry<Rect>>>() {
        /* class com.antlersoft.android.drawing.RectList.C00622 */

        /* access modifiers changed from: protected */
        @Override // com.antlersoft.util.ObjectPool
        public ArrayList<ObjectPool.Entry<Rect>> itemForPool() {
            return new ArrayList<>(8);
        }
    };
    private NonOverlappingPortion nonOverlappingPortion;
    private ObjectPool<NonOverlappingRects> nonOverlappingRectsPool = new ObjectPool<NonOverlappingRects>() {
        /* class com.antlersoft.android.drawing.RectList.C00611 */

        /* access modifiers changed from: protected */
        @Override // com.antlersoft.util.ObjectPool
        public NonOverlappingRects itemForPool() {
            return new NonOverlappingRects();
        }
    };
    private ObjectPool<Rect> pool;

    /* access modifiers changed from: package-private */
    public enum OverlapType {
        NONE,
        SAME,
        CONTAINS,
        CONTAINED_BY,
        COALESCIBLE,
        PARTIAL
    }

    /* access modifiers changed from: package-private */
    public static class NonOverlappingPortion {
        int adjacent;
        Rect bottomLeftPortion = new Rect();
        Rect bottomPortion = new Rect();
        Rect bottomRightPortion = new Rect();
        Rect coalesced = new Rect();
        int common;
        boolean horizontalOverlap;
        Rect leftPortion = new Rect();
        int r1Owns;
        int r2Owns;
        Rect rightPortion = new Rect();
        Rect topLeftPortion = new Rect();
        Rect topPortion = new Rect();
        Rect topRightPortion = new Rect();
        boolean verticalOverlap;

        NonOverlappingPortion() {
        }

        /* access modifiers changed from: package-private */
        public void setCornerOwnership(int side1, int side2, int corner) {
            int combined = side1 | side2;
            if ((this.r1Owns & combined) == combined) {
                this.r1Owns |= corner;
            } else if ((this.r2Owns & combined) == combined) {
                this.r2Owns |= corner;
            }
        }

        /* access modifiers changed from: package-private */
        public void setCornerOwnership() {
            setCornerOwnership(1, 4, 2);
            setCornerOwnership(4, 16, 8);
            setCornerOwnership(RectList.BOTTOM, 16, RectList.BOTTOM_RIGHT);
            setCornerOwnership(RectList.BOTTOM, 1, RectList.BOTTOM_LEFT);
        }

        /* access modifiers changed from: package-private */
        public OverlapType overlap(Rect r1, Rect r2) {
            this.r1Owns = 0;
            this.r2Owns = 0;
            this.common = 0;
            this.adjacent = 0;
            OverlapType result = OverlapType.NONE;
            this.horizontalOverlap = false;
            this.verticalOverlap = false;
            if (r1.left < r2.left) {
                Rect rect = this.leftPortion;
                Rect rect2 = this.topLeftPortion;
                Rect rect3 = this.bottomLeftPortion;
                int i = r1.left;
                rect3.left = i;
                rect2.left = i;
                rect.left = i;
                if (r2.left < r1.right) {
                    Rect rect4 = this.leftPortion;
                    Rect rect5 = this.topLeftPortion;
                    Rect rect6 = this.bottomLeftPortion;
                    Rect rect7 = this.topPortion;
                    Rect rect8 = this.bottomPortion;
                    int i2 = r2.left;
                    rect8.left = i2;
                    rect7.left = i2;
                    rect6.right = i2;
                    rect5.right = i2;
                    rect4.right = i2;
                    this.horizontalOverlap = true;
                } else {
                    Rect rect9 = this.leftPortion;
                    Rect rect10 = this.topLeftPortion;
                    Rect rect11 = this.bottomLeftPortion;
                    Rect rect12 = this.topPortion;
                    Rect rect13 = this.bottomPortion;
                    int i3 = r1.right;
                    rect13.left = i3;
                    rect12.left = i3;
                    rect11.right = i3;
                    rect10.right = i3;
                    rect9.right = i3;
                    if (r2.left == r1.right) {
                        this.adjacent |= 1;
                    }
                }
                this.r1Owns |= 1;
            } else {
                Rect rect14 = this.leftPortion;
                Rect rect15 = this.topLeftPortion;
                Rect rect16 = this.bottomLeftPortion;
                int i4 = r2.left;
                rect16.left = i4;
                rect15.left = i4;
                rect14.left = i4;
                if (r1.left < r2.right) {
                    Rect rect17 = this.leftPortion;
                    Rect rect18 = this.topLeftPortion;
                    Rect rect19 = this.bottomLeftPortion;
                    Rect rect20 = this.topPortion;
                    Rect rect21 = this.bottomPortion;
                    int i5 = r1.left;
                    rect21.left = i5;
                    rect20.left = i5;
                    rect19.right = i5;
                    rect18.right = i5;
                    rect17.right = i5;
                    this.horizontalOverlap = true;
                } else {
                    Rect rect22 = this.leftPortion;
                    Rect rect23 = this.topLeftPortion;
                    Rect rect24 = this.bottomLeftPortion;
                    Rect rect25 = this.topPortion;
                    Rect rect26 = this.bottomPortion;
                    int i6 = r2.right;
                    rect26.left = i6;
                    rect25.left = i6;
                    rect24.right = i6;
                    rect23.right = i6;
                    rect22.right = i6;
                    if (r1.left == r2.right) {
                        this.adjacent |= 16;
                    }
                }
                if (r2.left < r1.left) {
                    this.r2Owns |= 1;
                } else {
                    this.common |= 1;
                }
            }
            if (r1.top < r2.top) {
                Rect rect27 = this.topPortion;
                Rect rect28 = this.topLeftPortion;
                Rect rect29 = this.topRightPortion;
                int i7 = r1.top;
                rect29.top = i7;
                rect28.top = i7;
                rect27.top = i7;
                if (r2.top < r1.bottom) {
                    Rect rect30 = this.topPortion;
                    Rect rect31 = this.topLeftPortion;
                    Rect rect32 = this.topRightPortion;
                    Rect rect33 = this.leftPortion;
                    Rect rect34 = this.rightPortion;
                    int i8 = r2.top;
                    rect34.top = i8;
                    rect33.top = i8;
                    rect32.bottom = i8;
                    rect31.bottom = i8;
                    rect30.bottom = i8;
                    this.verticalOverlap = true;
                } else {
                    Rect rect35 = this.topPortion;
                    Rect rect36 = this.topLeftPortion;
                    Rect rect37 = this.topRightPortion;
                    Rect rect38 = this.leftPortion;
                    Rect rect39 = this.rightPortion;
                    int i9 = r1.bottom;
                    rect39.top = i9;
                    rect38.top = i9;
                    rect37.bottom = i9;
                    rect36.bottom = i9;
                    rect35.bottom = i9;
                    if (r2.top == r1.bottom) {
                        this.adjacent |= 4;
                    }
                }
                this.r1Owns |= 4;
            } else {
                Rect rect40 = this.topPortion;
                Rect rect41 = this.topLeftPortion;
                Rect rect42 = this.topRightPortion;
                int i10 = r2.top;
                rect42.top = i10;
                rect41.top = i10;
                rect40.top = i10;
                if (r1.top < r2.bottom) {
                    Rect rect43 = this.topPortion;
                    Rect rect44 = this.topLeftPortion;
                    Rect rect45 = this.topRightPortion;
                    Rect rect46 = this.leftPortion;
                    Rect rect47 = this.rightPortion;
                    int i11 = r1.top;
                    rect47.top = i11;
                    rect46.top = i11;
                    rect45.bottom = i11;
                    rect44.bottom = i11;
                    rect43.bottom = i11;
                    this.verticalOverlap = true;
                } else {
                    Rect rect48 = this.topPortion;
                    Rect rect49 = this.topLeftPortion;
                    Rect rect50 = this.topRightPortion;
                    Rect rect51 = this.leftPortion;
                    Rect rect52 = this.rightPortion;
                    int i12 = r2.bottom;
                    rect52.top = i12;
                    rect51.top = i12;
                    rect50.bottom = i12;
                    rect49.bottom = i12;
                    rect48.bottom = i12;
                    if (r1.top == r2.bottom) {
                        this.adjacent |= RectList.BOTTOM;
                    }
                }
                if (r2.top < r1.top) {
                    this.r2Owns |= 4;
                } else {
                    this.common |= 4;
                }
            }
            if (r1.right > r2.right) {
                Rect rect53 = this.rightPortion;
                Rect rect54 = this.topRightPortion;
                Rect rect55 = this.bottomRightPortion;
                int i13 = r1.right;
                rect55.right = i13;
                rect54.right = i13;
                rect53.right = i13;
                if (r2.right > r1.left) {
                    Rect rect56 = this.rightPortion;
                    Rect rect57 = this.topRightPortion;
                    Rect rect58 = this.bottomRightPortion;
                    Rect rect59 = this.topPortion;
                    Rect rect60 = this.bottomPortion;
                    int i14 = r2.right;
                    rect60.right = i14;
                    rect59.right = i14;
                    rect58.left = i14;
                    rect57.left = i14;
                    rect56.left = i14;
                    this.horizontalOverlap = true;
                } else {
                    Rect rect61 = this.rightPortion;
                    Rect rect62 = this.topRightPortion;
                    Rect rect63 = this.bottomRightPortion;
                    Rect rect64 = this.topPortion;
                    Rect rect65 = this.bottomPortion;
                    int i15 = r1.left;
                    rect65.right = i15;
                    rect64.right = i15;
                    rect63.left = i15;
                    rect62.left = i15;
                    rect61.left = i15;
                    if (r2.right == r1.left) {
                        this.adjacent |= 16;
                    }
                }
                this.r1Owns |= 16;
            } else {
                Rect rect66 = this.rightPortion;
                Rect rect67 = this.topRightPortion;
                Rect rect68 = this.bottomRightPortion;
                int i16 = r2.right;
                rect68.right = i16;
                rect67.right = i16;
                rect66.right = i16;
                if (r1.right > r2.left) {
                    Rect rect69 = this.rightPortion;
                    Rect rect70 = this.topRightPortion;
                    Rect rect71 = this.bottomRightPortion;
                    Rect rect72 = this.topPortion;
                    Rect rect73 = this.bottomPortion;
                    int i17 = r1.right;
                    rect73.right = i17;
                    rect72.right = i17;
                    rect71.left = i17;
                    rect70.left = i17;
                    rect69.left = i17;
                    this.horizontalOverlap = true;
                } else {
                    Rect rect74 = this.rightPortion;
                    Rect rect75 = this.topRightPortion;
                    Rect rect76 = this.bottomRightPortion;
                    Rect rect77 = this.topPortion;
                    Rect rect78 = this.bottomPortion;
                    int i18 = r2.left;
                    rect78.right = i18;
                    rect77.right = i18;
                    rect76.left = i18;
                    rect75.left = i18;
                    rect74.left = i18;
                    if (r1.right == r2.left) {
                        this.adjacent |= 1;
                    }
                }
                if (r2.right > r1.right) {
                    this.r2Owns |= 16;
                } else {
                    this.common |= 16;
                }
            }
            if (r1.bottom > r2.bottom) {
                Rect rect79 = this.bottomPortion;
                Rect rect80 = this.bottomLeftPortion;
                Rect rect81 = this.bottomRightPortion;
                int i19 = r1.bottom;
                rect81.bottom = i19;
                rect80.bottom = i19;
                rect79.bottom = i19;
                if (r2.bottom > r1.top) {
                    Rect rect82 = this.bottomPortion;
                    Rect rect83 = this.bottomLeftPortion;
                    Rect rect84 = this.bottomRightPortion;
                    Rect rect85 = this.leftPortion;
                    Rect rect86 = this.rightPortion;
                    int i20 = r2.bottom;
                    rect86.bottom = i20;
                    rect85.bottom = i20;
                    rect84.top = i20;
                    rect83.top = i20;
                    rect82.top = i20;
                    this.verticalOverlap = true;
                } else {
                    Rect rect87 = this.bottomPortion;
                    Rect rect88 = this.bottomLeftPortion;
                    Rect rect89 = this.bottomRightPortion;
                    Rect rect90 = this.leftPortion;
                    Rect rect91 = this.rightPortion;
                    int i21 = r1.top;
                    rect91.bottom = i21;
                    rect90.bottom = i21;
                    rect89.top = i21;
                    rect88.top = i21;
                    rect87.top = i21;
                    if (r2.bottom == r1.top) {
                        this.adjacent |= RectList.BOTTOM;
                    }
                }
                this.r1Owns |= RectList.BOTTOM;
            } else {
                Rect rect92 = this.bottomPortion;
                Rect rect93 = this.bottomLeftPortion;
                Rect rect94 = this.bottomRightPortion;
                int i22 = r2.bottom;
                rect94.bottom = i22;
                rect93.bottom = i22;
                rect92.bottom = i22;
                if (r1.bottom > r2.top) {
                    Rect rect95 = this.bottomPortion;
                    Rect rect96 = this.bottomLeftPortion;
                    Rect rect97 = this.bottomRightPortion;
                    Rect rect98 = this.leftPortion;
                    Rect rect99 = this.rightPortion;
                    int i23 = r1.bottom;
                    rect99.bottom = i23;
                    rect98.bottom = i23;
                    rect97.top = i23;
                    rect96.top = i23;
                    rect95.top = i23;
                    this.verticalOverlap = true;
                } else {
                    Rect rect100 = this.bottomPortion;
                    Rect rect101 = this.bottomLeftPortion;
                    Rect rect102 = this.bottomRightPortion;
                    Rect rect103 = this.leftPortion;
                    Rect rect104 = this.rightPortion;
                    int i24 = r2.top;
                    rect104.bottom = i24;
                    rect103.bottom = i24;
                    rect102.top = i24;
                    rect101.top = i24;
                    rect100.top = i24;
                    if (r1.bottom == r2.top) {
                        this.adjacent |= 4;
                    }
                }
                if (r2.bottom > r1.bottom) {
                    this.r2Owns |= RectList.BOTTOM;
                } else {
                    this.common |= RectList.BOTTOM;
                }
            }
            if (this.common == 85) {
                return OverlapType.SAME;
            }
            if ((this.common & 17) == 17 && (this.verticalOverlap || (this.adjacent & 68) != 0)) {
                OverlapType result2 = OverlapType.COALESCIBLE;
                this.coalesced.left = r1.left;
                this.coalesced.right = r1.right;
                this.coalesced.top = this.topPortion.top;
                this.coalesced.bottom = this.bottomPortion.bottom;
                return result2;
            } else if ((this.common & 68) == 68 && (this.horizontalOverlap || (this.adjacent & 17) != 0)) {
                OverlapType result3 = OverlapType.COALESCIBLE;
                this.coalesced.left = this.leftPortion.left;
                this.coalesced.right = this.rightPortion.right;
                this.coalesced.top = r1.top;
                this.coalesced.bottom = r1.bottom;
                return result3;
            } else if (!this.verticalOverlap || !this.horizontalOverlap) {
                return result;
            } else {
                if (this.r2Owns == 0) {
                    return OverlapType.CONTAINED_BY;
                }
                if (this.r1Owns == 0) {
                    return OverlapType.CONTAINS;
                }
                OverlapType result4 = OverlapType.PARTIAL;
                setCornerOwnership();
                return result4;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static class NonOverlappingRects {
        static final int MAX_RECTS = 8;
        int count;
        ObjectPool.Entry<Rect>[] rectEntries = new ObjectPool.Entry[8];

        NonOverlappingRects() {
        }

        private void addOwnedRect(int owner, int direction, ObjectPool<Rect> pool, Rect r) {
            if ((owner & direction) == direction) {
                ObjectPool.Entry<Rect> entry = pool.reserve();
                ObjectPool.Entry<Rect>[] entryArr = this.rectEntries;
                int i = this.count;
                this.count = i + 1;
                entryArr[i] = entry;
                entry.get().set(r);
            }
        }

        /* access modifiers changed from: package-private */
        public void Populate(NonOverlappingPortion p, ObjectPool<Rect> pool, int owner) {
            this.count = 0;
            for (int i = 0; i < 8; i++) {
                this.rectEntries[i] = null;
            }
            addOwnedRect(owner, RectList.BOTTOM_LEFT, pool, p.bottomLeftPortion);
            addOwnedRect(owner, RectList.BOTTOM, pool, p.bottomPortion);
            addOwnedRect(owner, RectList.BOTTOM_RIGHT, pool, p.bottomRightPortion);
            addOwnedRect(owner, RectList.BOTTOM, pool, p.bottomPortion);
            addOwnedRect(owner, 8, pool, p.topRightPortion);
            addOwnedRect(owner, 4, pool, p.topPortion);
            addOwnedRect(owner, 2, pool, p.topLeftPortion);
            addOwnedRect(owner, 1, pool, p.leftPortion);
        }
    }

    public RectList(ObjectPool<Rect> pool2) {
        this.pool = pool2;
        this.list = new ArrayList<>();
        this.nonOverlappingPortion = new NonOverlappingPortion();
    }

    public int getSize() {
        return this.list.size();
    }

    public Rect get(int i) {
        return this.list.get(i).get();
    }

    public void clear() {
        for (int i = this.list.size() - 1; i >= 0; i--) {
            this.pool.release(this.list.get(i));
        }
        this.list.clear();
    }

    private void recursiveAdd(ObjectPool.Entry<Rect> toAdd, int level) {
        if (level >= this.list.size()) {
            this.list.add(toAdd);
            return;
        }
        Rect addRect = toAdd.get();
        ObjectPool.Entry<Rect> thisEntry = this.list.get(level);
        switch (C00633.$SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[this.nonOverlappingPortion.overlap(thisEntry.get(), addRect).ordinal()]) {
            case 1:
                recursiveAdd(toAdd, level + 1);
                return;
            case 2:
            case 3:
                this.pool.release(toAdd);
                return;
            case 4:
                this.pool.release(thisEntry);
                this.list.remove(level);
                recursiveAdd(toAdd, level);
                return;
            case 5:
                this.pool.release(thisEntry);
                this.list.remove(level);
                addRect.set(this.nonOverlappingPortion.coalesced);
                recursiveAdd(toAdd, 0);
                return;
            case 6:
                this.pool.release(toAdd);
                ObjectPool.Entry<NonOverlappingRects> rectsEntry = this.nonOverlappingRectsPool.reserve();
                NonOverlappingRects rects = rectsEntry.get();
                rects.Populate(this.nonOverlappingPortion, this.pool, this.nonOverlappingPortion.r2Owns);
                for (int i = 0; i < rects.count; i++) {
                    recursiveAdd(rects.rectEntries[i], 0);
                }
                this.nonOverlappingRectsPool.release(rectsEntry);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.antlersoft.android.drawing.RectList$3 */
    public static /* synthetic */ class C00633 {
        static final /* synthetic */ int[] $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType = new int[OverlapType.values().length];

        static {
            try {
                $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[OverlapType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[OverlapType.SAME.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[OverlapType.CONTAINS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[OverlapType.CONTAINED_BY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[OverlapType.COALESCIBLE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[OverlapType.PARTIAL.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public void add(Rect toAdd) {
        ObjectPool.Entry<Rect> entry = this.pool.reserve();
        entry.get().set(toAdd);
        recursiveAdd(entry, 0);
    }

    public void intersect(Rect bounds) {
        int size = this.list.size();
        ObjectPool.Entry<ArrayList<ObjectPool.Entry<Rect>>> listEntry = this.listRectsPool.reserve();
        ArrayList<ObjectPool.Entry<Rect>> newList = listEntry.get();
        newList.clear();
        for (int i = 0; i < size; i++) {
            ObjectPool.Entry<Rect> entry = this.list.get(i);
            if (entry.get().intersect(bounds)) {
                newList.add(entry);
            } else {
                this.pool.release(entry);
            }
        }
        this.list.clear();
        int size2 = newList.size();
        for (int i2 = 0; i2 < size2; i2++) {
            recursiveAdd(newList.get(i2), 0);
        }
        this.listRectsPool.release(listEntry);
    }

    public boolean testIntersect(Rect r) {
        int l = this.list.size();
        for (int i = 0; i < l; i++) {
            if (this.list.get(i).get().intersects(r.left, r.top, r.right, r.bottom)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public void subtract(Rect toSubtract) {
        int size = this.list.size();
        ObjectPool.Entry<ArrayList<ObjectPool.Entry<Rect>>> listEntry = this.listRectsPool.reserve();
        ArrayList<ObjectPool.Entry<Rect>> newList = listEntry.get();
        newList.clear();
        int i = 0;
        while (i < size) {
            ObjectPool.Entry<Rect> entry = this.list.get(i);
            switch (C00633.$SwitchMap$com$antlersoft$android$drawing$RectList$OverlapType[this.nonOverlappingPortion.overlap(entry.get(), toSubtract).ordinal()]) {
                case 1:
                default:
                    i++;
                case 2:
                    this.pool.release(entry);
                    newList.clear();
                    this.list.remove(i);
                    return;
                case 3:
                    this.nonOverlappingPortion.setCornerOwnership();
                    break;
                case 4:
                    this.pool.release(entry);
                    this.list.remove(i);
                    i--;
                    size--;
                    i++;
                    continue;
                case 5:
                    i++;
                    if (this.nonOverlappingPortion.verticalOverlap) {
                        if (!this.nonOverlappingPortion.horizontalOverlap) {
                        }
                        this.nonOverlappingPortion.setCornerOwnership();
                        break;
                    } else {
                        continue;
                    }
                case 6:
                    break;
            }
            ObjectPool.Entry<NonOverlappingRects> rectsEntry = this.nonOverlappingRectsPool.reserve();
            NonOverlappingRects rects = rectsEntry.get();
            rects.Populate(this.nonOverlappingPortion, this.pool, this.nonOverlappingPortion.r1Owns);
            this.pool.release(entry);
            this.list.remove(i);
            i--;
            size--;
            for (int j = 0; j < rects.count; j++) {
                newList.add(rects.rectEntries[j]);
            }
            this.nonOverlappingRectsPool.release(rectsEntry);
            i++;
        }
        int size2 = newList.size();
        for (int i2 = 0; i2 < size2; i2++) {
            recursiveAdd(newList.get(i2), 0);
        }
        this.listRectsPool.release(listEntry);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (int i = 0; i < getSize(); i++) {
            sb.append(get(i).toString());
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
