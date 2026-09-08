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

public class PowFunction implements RulerFunction {

    @Override
    public String getName() {
        return "pow";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            return null;
        }
        double base = toDouble((BaseValue) param[0]);
        double exponent = toDouble((BaseValue) param[1]);
        return new DoubleValue(BigDecimal.valueOf(Math.pow(base, exponent)));
    }

    private double toDouble(BaseValue value) {
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue().doubleValue();
        } else if (value.getType() == ValueType.DOUBLE) {
            return ((DoubleValue) value).getValue().doubleValue();
        }
        throw new RulerRuntimeException("pow expects numbers");
    }
}
