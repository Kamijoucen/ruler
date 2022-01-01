package com.kamijoucen.ruler.value;

import java.util.List;

import com.kamijoucen.ruler.runtime.RClassInfo;

public class ArrayValue extends AbstractRClassValue {

    private List<BaseValue> values;

    public ArrayValue(List<BaseValue> values, RClassInfo mataData) {
        super(mataData);
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
