package com.antlersoft.util;

public class ByteBufferStack {
    public static final int MAX_DEPTH = 20;
    public static final int MAX_SIZE = 1048;
    private byte[] m_buffer;
    private int m_depth;
    private int m_max_depth;
    private int m_max_size;
    private int[] m_offsets;

    public ByteBufferStack(int maxDepth, int maxSize) {
        this.m_depth = 0;
        this.m_max_depth = maxDepth;
        this.m_max_size = maxSize;
        this.m_offsets = new int[maxDepth];
        this.m_buffer = new byte[maxSize];
    }

    public ByteBufferStack() {
        this(20, MAX_SIZE);
    }

    public byte[] getBuffer() {
        return this.m_buffer;
    }

    public int getOffset() {
        return this.m_offsets[this.m_depth];
    }

    public int reserve(int count) {
        if (count < 0 || this.m_max_size + count < 0) {
            throw new IllegalArgumentException("Count must by greater than 0");
        }
        if (this.m_depth == this.m_max_depth) {
            this.m_max_depth *= 2;
            int[] new_offsets = new int[this.m_max_depth];
            System.arraycopy(this.m_offsets, 0, new_offsets, 0, this.m_depth);
            this.m_offsets = new_offsets;
        }
        int result = this.m_offsets[this.m_depth];
        int new_size = result + count;
        int[] iArr = this.m_offsets;
        int i = this.m_depth;
        this.m_depth = i + 1;
        iArr[i] = new_size;
        if (new_size > this.m_max_size) {
            this.m_max_size = Math.max(this.m_max_size * 2, new_size);
            byte[] new_buffer = new byte[this.m_max_size];
            System.arraycopy(this.m_buffer, 0, new_buffer, 0, result);
            this.m_buffer = new_buffer;
        }
        return result;
    }

    public void release() {
        if (this.m_depth < 1) {
            throw new IllegalStateException("release() without reserve()");
        }
        this.m_depth--;
    }
}
