package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.common.RMateInfo;
import com.kamijoucen.ruler.value.DateValue;
import com.kamijoucen.ruler.value.IntegerValue;

import java.util.Calendar;

public class DateGetYearFun extends AbstractMetaFun {

    public DateGetYearFun(RMateInfo mateInfo) {
        super(mateInfo);
    }

    @Override
    public String getName() {
        return "year";
    }

    @Override
    public Object call(Object... param) {
        DateValue source = (DateValue) this.getMetaInfo().getSource();
        return new IntegerValue(source.getCalendar().get(Calendar.YEAR));
    }
}
