package com.kamijoucen.ruler.value;

import java.util.List;

import com.kamijoucen.ruler.runtime.MataData;

public interface MataValue extends BaseValue {

    MataData getMataData();

    BaseValue invoke(String name, List<BaseValue> param);

    BaseValue getProperty(String name);

}
