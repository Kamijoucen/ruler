package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.common.RClassInfo;
import com.kamijoucen.ruler.value.BaseValue;

public interface MetaClassFactory {

    RClassInfo createClassInfo(BaseValue baseValue);

}
