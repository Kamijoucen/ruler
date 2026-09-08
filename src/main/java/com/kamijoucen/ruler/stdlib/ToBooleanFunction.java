package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class ToBooleanFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ToBoolean";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
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
        throw new IllegalArgumentException("cannot convert " + baseValue + " to boolean");
    }

}
