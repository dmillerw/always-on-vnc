package android.androidVNC;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ZlibInStream extends InStream {
    static final int defaultBufSize = 16384;
    private int bufSize;
    private int bytesIn;
    private Inflater inflater;
    private int ptrOffset;
    private InStream underlying;

    public ZlibInStream(int bufSize_) {
        this.bufSize = bufSize_;
        this.f4b = new byte[this.bufSize];
        this.ptrOffset = 0;
        this.end = 0;
        this.ptr = 0;
        this.inflater = new Inflater();
    }

    public ZlibInStream() {
        this(defaultBufSize);
    }

    public void setUnderlying(InStream is, int bytesIn_) {
        this.underlying = is;
        this.bytesIn = bytesIn_;
        this.end = 0;
        this.ptr = 0;
    }

    public void reset() throws Exception {
        this.end = 0;
        this.ptr = 0;
        if (this.underlying != null) {
            while (this.bytesIn > 0) {
                decompress();
                this.end = 0;
            }
            this.underlying = null;
        }
    }

    @Override // android.androidVNC.InStream
    public int pos() {
        return this.ptrOffset + this.ptr;
    }

    /* access modifiers changed from: protected */
    @Override // android.androidVNC.InStream
    public int overrun(int itemSize, int nItems) throws Exception {
        if (itemSize > this.bufSize) {
            throw new Exception("ZlibInStream overrun: max itemSize exceeded");
        } else if (this.underlying == null) {
            throw new Exception("ZlibInStream overrun: no underlying stream");
        } else {
            if (this.end - this.ptr != 0) {
                System.arraycopy(this.f4b, this.ptr, this.f4b, 0, this.end - this.ptr);
            }
            this.ptrOffset += this.ptr;
            this.end -= this.ptr;
            this.ptr = 0;
            while (this.end < itemSize) {
                decompress();
            }
            if (itemSize * nItems > this.end) {
                return this.end / itemSize;
            }
            return nItems;
        }
    }

    private void decompress() throws Exception {
        try {
            this.underlying.check(1);
            int avail_in = this.underlying.getend() - this.underlying.getptr();
            if (avail_in > this.bytesIn) {
                avail_in = this.bytesIn;
            }
            if (this.inflater.needsInput()) {
                this.inflater.setInput(this.underlying.getbuf(), this.underlying.getptr(), avail_in);
            }
            this.end += this.inflater.inflate(this.f4b, this.end, this.bufSize - this.end);
            if (this.inflater.needsInput()) {
                this.bytesIn -= avail_in;
                this.underlying.setptr(this.underlying.getptr() + avail_in);
            }
        } catch (DataFormatException e) {
            throw new Exception("ZlibInStream: inflate failed");
        }
    }
}
