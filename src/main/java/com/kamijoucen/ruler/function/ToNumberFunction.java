package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.util.ConvertUtil;

public class ToNumberFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ToNumber";
    }

    @Override
    public Object call(Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        return ConvertUtil.stringToValue(String.valueOf(param[0]));
    }

}
