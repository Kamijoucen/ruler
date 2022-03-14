package com.kamijoucen.ruler.function;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFunction implements RulerFunction {

    @Override
    public String getName() {
        return "datetime";
    }

    @Override
    public Object call(Object... param) {
        String date = (String) param[0];
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if (param.length > 1) {
            pattern = (String) param[1];
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
