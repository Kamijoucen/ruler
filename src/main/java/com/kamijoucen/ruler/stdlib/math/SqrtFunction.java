package com.kamijoucen.ruler.stdlib.math;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DoubleValue;
import com.kamijoucen.ruler.types.value.IntegerValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.spi.RulerFunction;

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
