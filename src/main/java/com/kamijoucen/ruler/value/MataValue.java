package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RClassInfo;
import com.kamijoucen.ruler.runtime.RuntimeContext;

import java.util.List;

public interface MataValue extends BaseValue {

    RClassInfo getClassInfo();

    BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param);

    BaseValue getProperty(String name);

}
