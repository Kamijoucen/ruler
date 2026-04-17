package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.math.BigDecimal;

public class SqrtFunction implements RulerFunction {

    @Override
    public String getName() {
        return "sqrt";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        BaseValue value = (BaseValue) param[0];
        double d;
        if (value.getType() == ValueType.INTEGER) {
            d = ((IntegerValue) value).getValue().doubleValue();
        } else if (value.getType() == ValueType.DOUBLE) {
            d = ((DoubleValue) value).getValue().doubleValue();
        } else {
            throw new RulerRuntimeException("sqrt expects a number");
        }
        return new DoubleValue(BigDecimal.valueOf(Math.sqrt(d)));
    }
}
