package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.common.RMateInfo;

public abstract class AbstractMetaFun implements MetaFun {

    private RMateInfo mateInfo;

    public AbstractMetaFun(RMateInfo mateInfo) {
        this.mateInfo = mateInfo;
    }

    @Override
    public RMateInfo getMetaInfo() {
        if (mateInfo == null) {
            throw new UnsupportedOperationException();
        }
        return this.mateInfo;
    }
}
