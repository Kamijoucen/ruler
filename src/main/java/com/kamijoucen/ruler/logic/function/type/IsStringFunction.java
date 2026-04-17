package com.kamijoucen.ruler.logic.function.type;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

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
