package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.ValueType;

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
