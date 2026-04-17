package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class AbsFunction implements RulerFunction {

    @Override
    public String getName() {
        return "abs";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        BaseValue value = (BaseValue) param[0];
        if (value.getType() == ValueType.INTEGER) {
            long v = ((IntegerValue) value).getValue();
            return context.getConfiguration().getIntegerNumberCache().getValue(Math.abs(v));
        } else if (value.getType() == ValueType.DOUBLE) {
            return new DoubleValue(Math.abs(((DoubleValue) value).getValue()));
        }
        throw new RulerRuntimeException("abs expects a number");
    }
}
