package com.kamijoucen.ruler.value;

import java.util.List;

public class ArrayValue extends AbstractValue {

    private List<BaseValue> values;

    public ArrayValue(List<BaseValue> values, RClass classValue) {
        super(classValue);
        this.values = values;
    }

    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

    public List<BaseValue> getValues() {
        return values;
    }

    public void setValues(List<BaseValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
