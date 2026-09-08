package com.kamijoucen.ruler.stdlib.type;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.spi.RulerFunction;

public class IsStringFunction implements RulerFunction {

    @Override
    public String getName() {
        return "isString";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return BoolValue.get(false);
        }
        return BoolValue.get(((BaseValue) param[0]).getType() == ValueType.STRING);
    }
}
