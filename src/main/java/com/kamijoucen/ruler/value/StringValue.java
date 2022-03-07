package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RClassInfo;

public class StringValue extends AbstractRClassValue {

    private String value;

    public StringValue(String value) {
        super(null);
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
