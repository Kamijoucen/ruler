package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DateValue;

import java.util.Calendar;

public class DateGetYearFun extends AbstractMetaFun {

    @Override
    public String getName() {
        return "year";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        DateValue source = (DateValue) this.getSource();
        return context.getConfiguration().getIntegerNumberCache().getValue(source.getCalendar().get(Calendar.YEAR));
    }
}
