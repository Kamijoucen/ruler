package com.kamijoucen.ruler.stdlib.type;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.spi.RulerFunction;

public class IsFunctionFunction implements RulerFunction {

    @Override
    public String getName() {
        return "isFunction";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return BoolValue.get(false);
        }
        ValueType type = ((BaseValue) param[0]).getType();
        return BoolValue.get(type == ValueType.FUNCTION || type == ValueType.CLOSURE || type == ValueType.METHOD);
    }
}
