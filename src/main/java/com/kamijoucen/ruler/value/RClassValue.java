package com.kamijoucen.ruler.value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RClassValue extends AbstractValue implements RClass {

    // todo _properties_ 到底指代的什么，是类中properties本身，还是properties中的一个字段？
    private final Map<String, BaseValue> properties = new ConcurrentHashMap<String, BaseValue>();

    public RClassValue(RClass classInfo) {
        super(classInfo);
    }

    @Override
    public RClass getRClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, BaseValue> getProperties() {
        return null;
    }

    @Override
    public BaseValue getProperty(String name) {

        return null;
    }

    @Override
    public ValueType getType() {
        return ValueType.RCLASS;
    }
}
