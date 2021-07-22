package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.MataData;

public interface MataValue extends BaseValue {

    MataData getMataData();

    BaseValue invoke(String name);

    BaseValue getProperty(String name);

}
