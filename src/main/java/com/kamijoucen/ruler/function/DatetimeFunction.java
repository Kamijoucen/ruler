package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Datetime";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param.length == 0) {
            return new Date();
        }
        if (!(param[0] instanceof String)) {
            throw new RuntimeException("datetime function only accept string type");
        }
        String date = (String) param[0];
        String pattern;
        if (param.length > 1) {
            pattern = (String) param[1];
        } else {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException ignored) {
        }
        return null;
    }
}
