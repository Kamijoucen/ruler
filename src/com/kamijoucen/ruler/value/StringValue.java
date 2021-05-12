package com.kamijoucen.ruler.value;

public class StringValue implements BaseValue {

    private String value;

    public StringValue(String value) {
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
}
