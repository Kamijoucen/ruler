package com.kamijoucen.ruler.value;

import java.util.Map;

public interface Rson extends BaseValue {

    BaseValue getField(String name);

    void putField(String name, BaseValue baseValue);

    Map<String, BaseValue> getFields();

}
