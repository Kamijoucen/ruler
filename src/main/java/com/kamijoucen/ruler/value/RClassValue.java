package com.kamijoucen.ruler.value;

import java.util.Map;

public interface RClassValue extends BaseValue {

    Map<String, BaseValue> getProperties();

    BaseValue getProperty(String name);

}
