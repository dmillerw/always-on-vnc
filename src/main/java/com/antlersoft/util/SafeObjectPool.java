package com.antlersoft.util;

import com.antlersoft.util.ObjectPool;

public abstract class SafeObjectPool<R> extends ObjectPool<R> {
    @Override // com.antlersoft.util.ObjectPool
    public synchronized void release(ObjectPool.Entry<R> entry) {
        super.release(entry);
    }

    @Override // com.antlersoft.util.ObjectPool
    public synchronized ObjectPool.Entry<R> reserve() {
        return super.reserve();
    }
}
