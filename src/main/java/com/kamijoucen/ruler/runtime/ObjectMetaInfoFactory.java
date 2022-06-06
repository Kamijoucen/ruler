package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.RMateInfo;
import com.kamijoucen.ruler.config.MetaInfoFactory;
import com.kamijoucen.ruler.runtime.metafun.DateGetYearFun;
import com.kamijoucen.ruler.value.FunctionValue;

public class ObjectMetaInfoFactory implements MetaInfoFactory {

    @Override
    public RMateInfo createDateMateInfo() {
        RMateInfo mateInfo = new RMateInfo();
        DateGetYearFun getYearFun = new DateGetYearFun(mateInfo);

        mateInfo.put(getYearFun.getName(), new FunctionValue(getYearFun));
        return mateInfo;
    }
}
