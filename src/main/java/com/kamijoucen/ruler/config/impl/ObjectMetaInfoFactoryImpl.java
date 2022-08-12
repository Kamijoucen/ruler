package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.config.MetaInfoFactory;
import com.kamijoucen.ruler.runtime.metafun.ArrayPushFun;
import com.kamijoucen.ruler.runtime.metafun.ArraySizeFun;
import com.kamijoucen.ruler.runtime.metafun.DateGetYearFun;

public class ObjectMetaInfoFactoryImpl implements MetaInfoFactory {

    @Override
    public RMetaInfo createDateMetaInfo() {
        RMetaInfo metaInfo = new RMetaInfo();
        metaInfo.initMetaFun(new DateGetYearFun());
        return metaInfo;
    }

    @Override
    public RMetaInfo createArrayMetaInfo() {
        RMetaInfo metaInfo = new RMetaInfo();
        metaInfo.initMetaFun(new ArraySizeFun());
        metaInfo.initMetaFun(new ArrayPushFun());
        return metaInfo;
    }
}
