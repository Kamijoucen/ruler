package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.logic.util.ConvertUtil;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class ToNumberFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ToNumber";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        BaseValue baseValue = (BaseValue) param[0];
        if (baseValue.getType() == ValueType.INTEGER
                || baseValue.getType() == ValueType.DOUBLE) {
            return baseValue;
        }
        return ConvertUtil.stringToValue(String.valueOf(baseValue), context);
    }

}
