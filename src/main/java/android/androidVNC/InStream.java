package android.androidVNC;

public abstract class InStream {

    /* renamed from: b */
    protected byte[] f4b;
    protected int end;
    protected int ptr;

    /* access modifiers changed from: protected */
    public abstract int overrun(int i, int i2) throws Exception;

    public abstract int pos();

    public final int check(int itemSize, int nItems) throws Exception {
        if (this.ptr + (itemSize * nItems) > this.end) {
            if (this.ptr + itemSize > this.end) {
                return overrun(itemSize, nItems);
            }
            nItems = (this.end - this.ptr) / itemSize;
        }
        return nItems;
    }

    public final void check(int itemSize) throws Exception {
        if (this.ptr + itemSize > this.end) {
            overrun(itemSize, 1);
        }
    }

    public final int readS8() throws Exception {
        check(1);
        byte[] bArr = this.f4b;
        int i = this.ptr;
        this.ptr = i + 1;
        return bArr[i];
    }

    public final int readS16() throws Exception {
        check(2);
        byte[] bArr = this.f4b;
        int i = this.ptr;
        this.ptr = i + 1;
        byte b = bArr[i];
        byte[] bArr2 = this.f4b;
        int i2 = this.ptr;
        this.ptr = i2 + 1;
        return (b << 8) | (bArr2[i2] & 255);
    }

    public final int readS32() throws Exception {
        check(4);
        byte[] bArr = this.f4b;
        int i = this.ptr;
        this.ptr = i + 1;
        byte b = bArr[i];
        byte[] bArr2 = this.f4b;
        int i2 = this.ptr;
        this.ptr = i2 + 1;
        int b1 = bArr2[i2] & 255;
        byte[] bArr3 = this.f4b;
        int i3 = this.ptr;
        this.ptr = i3 + 1;
        int b2 = bArr3[i3] & 255;
        byte[] bArr4 = this.f4b;
        int i4 = this.ptr;
        this.ptr = i4 + 1;
        return (b << 24) | (b1 << 16) | (b2 << 8) | (bArr4[i4] & 255);
    }

    public final int readU8() throws Exception {
        return readS8() & 255;
    }

    public final int readU16() throws Exception {
        return readS16() & 65535;
    }

    public final int readU32() throws Exception {
        return readS32() & -1;
    }

    public final void skip(int bytes) throws Exception {
        while (bytes > 0) {
            int n = check(1, bytes);
            this.ptr += n;
            bytes -= n;
        }
    }

    public void readBytes(byte[] data, int offset, int length) throws Exception {
        int offsetEnd = offset + length;
        while (offset < offsetEnd) {
            int n = check(1, offsetEnd - offset);
            System.arraycopy(this.f4b, this.ptr, data, offset, n);
            this.ptr += n;
            offset += n;
        }
    }

    public final int readOpaque8() throws Exception {
        return readU8();
    }

    public final int readOpaque16() throws Exception {
        return readU16();
    }

    public final int readOpaque32() throws Exception {
        return readU32();
    }

    public final int readOpaque24A() throws Exception {
        check(3);
        byte[] bArr = this.f4b;
        int i = this.ptr;
        this.ptr = i + 1;
        byte b = bArr[i];
        byte[] bArr2 = this.f4b;
        int i2 = this.ptr;
        this.ptr = i2 + 1;
        byte b2 = bArr2[i2];
        byte[] bArr3 = this.f4b;
        int i3 = this.ptr;
        this.ptr = i3 + 1;
        return (b << 24) | (b2 << 16) | (bArr3[i3] << 8);
    }

    public final int readOpaque24B() throws Exception {
        check(3);
        byte[] bArr = this.f4b;
        int i = this.ptr;
        this.ptr = i + 1;
        byte b = bArr[i];
        byte[] bArr2 = this.f4b;
        int i2 = this.ptr;
        this.ptr = i2 + 1;
        byte b2 = bArr2[i2];
        byte[] bArr3 = this.f4b;
        int i3 = this.ptr;
        this.ptr = i3 + 1;
        return (b << 16) | (b2 << 8) | bArr3[i3];
    }

    public boolean bytesAvailable() {
        return this.end != this.ptr;
    }

    public final byte[] getbuf() {
        return this.f4b;
    }

    public final int getptr() {
        return this.ptr;
    }

    public final int getend() {
        return this.end;
    }

    public final void setptr(int p) {
        this.ptr = p;
    }

    protected InStream() {
    }
}
