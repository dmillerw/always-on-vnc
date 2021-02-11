package android.androidVNC;

public class MemInStream extends InStream {
    public MemInStream(byte[] data, int offset, int len) {
        this.f4b = data;
        this.ptr = offset;
        this.end = offset + len;
    }

    @Override // android.androidVNC.InStream
    public int pos() {
        return this.ptr;
    }

    /* access modifiers changed from: protected */
    @Override // android.androidVNC.InStream
    public int overrun(int itemSize, int nItems) throws Exception {
        throw new Exception("MemInStream overrun: end of stream");
    }
}
