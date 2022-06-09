package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.value.BaseValue;

public abstract class AbstractMetaFun implements MetaFun {

    private RMetaInfo metaInfo;

    @Override
    public RMetaInfo getMetaInfo() {
        if (metaInfo == null) {
            throw new UnsupportedOperationException();
        }
        return this.metaInfo;
    }

    public void setMetaInfo(RMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public BaseValue getSource() {
        return this.getMetaInfo().getSource();
    }
}
