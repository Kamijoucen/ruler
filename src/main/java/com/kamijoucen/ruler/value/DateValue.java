package com.kamijoucen.ruler.value;

import java.util.Date;

public class DateValue implements BaseValue {

    private Date value;

    public DateValue(Date value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.DATE;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
