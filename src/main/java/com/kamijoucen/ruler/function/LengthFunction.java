package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class LengthFunction implements RulerFunction {

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public Object call(Object... param) {
        BaseValue value = (BaseValue) param[0];
        if (value.getType() == ValueType.ARRAY) {
            return ((ArrayValue) value).getValues().size();
        }
        return null;
    }

}
