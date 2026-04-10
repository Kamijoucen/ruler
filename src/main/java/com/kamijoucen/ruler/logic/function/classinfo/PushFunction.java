package com.kamijoucen.ruler.logic.function.classinfo;

import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class PushFunction implements RulerFunction {

    @Override
    public String getName() {
        return "push";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (self.getType() != ValueType.ARRAY) {
            throw new IllegalArgumentException("push function can only be called by array");
        }
        if (param == null || param.length == 0) {
            return null;
        }
        ArrayValue arrayValue = ((ArrayValue) self);
        arrayValue.getValues().add((BaseValue) param[0]);
        return param[0];
    }

}
