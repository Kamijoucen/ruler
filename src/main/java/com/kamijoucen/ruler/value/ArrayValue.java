package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RMetaInfo;

import java.util.List;

public class ArrayValue extends AbstractRClassValue {

    private List<BaseValue> values;

    public ArrayValue(List<BaseValue> values, RMetaInfo mataData) {
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
