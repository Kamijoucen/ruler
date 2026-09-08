package com.kamijoucen.ruler.stdlib.classinfo;

import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class PushFunction implements RulerFunction {

    @Override
    public String getName() {
        return "push";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (self.getType() != ValueType.ARRAY) {
            throw new IllegalArgumentException("push can only be called on an array");
        }
        if (param == null || param.length == 0) {
            return null;
        }
        ArrayValue arrayValue = ((ArrayValue) self);
        arrayValue.getValues().add((BaseValue) param[0]);
        return param[0];
    }

}
