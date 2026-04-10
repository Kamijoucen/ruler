package com.kamijoucen.ruler.domain.value;

import java.util.Map;

public interface RClass extends BaseValue {

    Map<String, BaseValue> getProperties();

    BaseValue getProperty(String name);

    void putProperty(String name, BaseValue value);

}
