package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.RClassInfo;
import com.kamijoucen.ruler.value.BaseValue;

public interface MetaClassFactory {

    RClassInfo createClassInfo(BaseValue baseValue);

}
