package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.RuntimeContext;

import java.util.List;

public interface MataValue extends BaseValue {

    MataData getMataData();

    BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param);

    BaseValue getProperty(String name);

}
