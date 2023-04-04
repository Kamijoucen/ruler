package com.kamijoucen.ruler.value;

import java.util.Map;

// add是没有，需要添加一个
// set是有key，但是没有value
// put是没有key

public interface RClass extends BaseValue {

    Map<String, BaseValue> getProperties();

    BaseValue getProperty(String name);

    void putProperty(String name, BaseValue value);

}
