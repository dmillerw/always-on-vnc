package com.antlersoft.android.dbimpl;

import com.antlersoft.android.dbimpl.ImplementationBase;

public interface NewInstance<E extends ImplementationBase> {
    E get();
}
