package com.kamijoucen.ruler.value;


public interface MataValue extends BaseValue {

    Object getMataData();

    BaseValue invoke(String name);

    BaseValue getProperty(String name);

}
