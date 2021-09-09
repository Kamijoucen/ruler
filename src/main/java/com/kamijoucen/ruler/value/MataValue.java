package com.kamijoucen.ruler.value;

import java.util.List;

import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.RuntimeContext;

public interface MataValue extends BaseValue {

    MataData getMataData();

    BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param);

    BaseValue getProperty(String name);

}
