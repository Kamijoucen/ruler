package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.runtime.RuntimeContext;

import java.util.List;

public interface MetaValue extends BaseValue {

    RMetaInfo getClassInfo();

    BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param);

    BaseValue getProperty(String name);

}
