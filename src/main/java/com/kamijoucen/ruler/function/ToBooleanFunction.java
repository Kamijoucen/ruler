package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class ToBooleanFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ToBoolean";
    }

    @Override
    public Object call(Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        BaseValue baseValue = (BaseValue) param[0];
        if (baseValue.getType() == ValueType.BOOL) {
            return baseValue;
        }
        String strVal = baseValue.toString();
        if (strVal.equalsIgnoreCase("true") || strVal.equalsIgnoreCase("false")) {
            return BoolValue.get(strVal.equalsIgnoreCase("true"));
        }
        throw new IllegalArgumentException("ToBoolean function can not convert " + baseValue + " to boolean");
    }

}
