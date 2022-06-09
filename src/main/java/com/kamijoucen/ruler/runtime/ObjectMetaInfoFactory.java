package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.config.MetaInfoFactory;
import com.kamijoucen.ruler.runtime.metafun.DateGetYearFun;

public class ObjectMetaInfoFactory implements MetaInfoFactory {

    @Override
    public RMetaInfo createDateMetaInfo() {
        RMetaInfo metaInfo = new RMetaInfo();
        metaInfo.initMetaFun(new DateGetYearFun());

        return metaInfo;
    }
}
