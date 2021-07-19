package com.kamijoucen.ruler.mata;

import com.kamijoucen.ruler.value.BaseValue;

public interface MataObject {

    MataData getMataData();

    BaseValue invoke(String name);

    BaseValue getProperty(String name);

}
