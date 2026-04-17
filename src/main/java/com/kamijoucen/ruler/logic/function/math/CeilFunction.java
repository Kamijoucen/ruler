package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CeilFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ceil";
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
            BigDecimal v = ((DoubleValue) value).getValue();
            return context.getConfiguration().getIntegerNumberCache().getValue(v.setScale(0, RoundingMode.CEILING).toBigIntegerExact());
        }
        throw new RulerRuntimeException("ceil expects a number");
    }
}
