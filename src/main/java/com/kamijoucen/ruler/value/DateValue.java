package com.kamijoucen.ruler.value;

import java.util.Calendar;
import java.util.Date;

public class DateValue extends RsonValue {

    private Date value;
    private Calendar calendar;

    public DateValue(Date value, RClassValue classValue) {
        super(classValue);
        this.value = value;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(value);
    }

    @Override
    public ValueType getType() {
        return ValueType.DATE;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
