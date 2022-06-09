package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RMetaInfo;

import java.util.Calendar;
import java.util.Date;

public class DateValue extends RsonValue {

    private Date value;
    private Calendar calendar;

    public DateValue(Date value, RMetaInfo metaInfo) {
        super(metaInfo);
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
