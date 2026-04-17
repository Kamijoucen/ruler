package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class MaxFunction implements RulerFunction {

    @Override
    public String getName() {
        return "max";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        double max = -Double.MAX_VALUE;
        boolean hasDouble = false;
        for (Object p : param) {
            BaseValue value = (BaseValue) p;
            if (value.getType() == ValueType.DOUBLE) {
                hasDouble = true;
                max = Math.max(max, ((DoubleValue) value).getValue());
            } else if (value.getType() == ValueType.INTEGER) {
                max = Math.max(max, ((IntegerValue) value).getValue());
            } else {
                throw new RulerRuntimeException("max expects numbers");
            }
        }
        if (hasDouble) {
            return new DoubleValue(max);
        }
        return context.getConfiguration().getIntegerNumberCache().getValue((long) max);
    }
}
