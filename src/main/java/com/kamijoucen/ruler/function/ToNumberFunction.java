package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

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
        BaseValue baseValue = (BaseValue) param[0];
        if (baseValue.getType() == ValueType.INTEGER
                || baseValue.getType() == ValueType.DOUBLE) {
            return baseValue;
        }
        return ConvertUtil.stringToValue(String.valueOf(baseValue));
    }

}
