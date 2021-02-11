package com.antlersoft.util;

public abstract class ObjectPool<R> {
    private Entry<R> next = null;

    /* access modifiers changed from: protected */
    public abstract R itemForPool();

    public static class Entry<S> {
        S item;
        Entry<S> nextEntry;

        Entry(S i, Entry<S> n) {
            this.item = i;
            this.nextEntry = n;
        }

        public S get() {
            return this.item;
        }
    }

    /* JADX DEBUG: Type inference failed for r1v1. Raw type applied. Possible types: com.antlersoft.util.ObjectPool$Entry<S>, com.antlersoft.util.ObjectPool$Entry<R> */
    public Entry<R> reserve() {
        if (this.next == null) {
            this.next = new Entry<>(itemForPool(), null);
        }
        Entry<R> result = this.next;
        this.next = (Entry<R>) result.nextEntry;
        result.nextEntry = null;
        return result;
    }

    /* JADX DEBUG: Type inference failed for r0v0. Raw type applied. Possible types: com.antlersoft.util.ObjectPool$Entry<R>, com.antlersoft.util.ObjectPool$Entry<S> */
    public void release(Entry<R> entry) {
        entry.nextEntry = (Entry<R>) this.next;
        this.next = entry;
    }
}
