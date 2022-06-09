package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.value.DateValue;
import com.kamijoucen.ruler.value.IntegerValue;

import java.util.Calendar;

public class DateGetYearFun extends AbstractMetaFun {

    @Override
    public String getName() {
        return "year";
    }

    @Override
    public Object call(Object... param) {
        DateValue source = (DateValue) this.getSource();
        return new IntegerValue(source.getCalendar().get(Calendar.YEAR));
    }
}
