package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class FloorFunction implements RulerFunction {

    @Override
    public String getName() {
        return "floor";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        BaseValue value = (BaseValue) param[0];
        if (value.getType() == ValueType.INTEGER) {
            return value;
        } else if (value.getType() == ValueType.DOUBLE) {
            return context.getConfiguration().getIntegerNumberCache().getValue((long) Math.floor(((DoubleValue) value).getValue()));
        }
        throw new RulerRuntimeException("floor expects a number");
    }
}
