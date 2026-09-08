package com.kamijoucen.ruler.stdlib.math;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DoubleValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundFunction implements RulerFunction {

    @Override
    public String getName() {
        return "round";
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
            return IntegerValue.valueOf(v.setScale(0, RoundingMode.HALF_UP).toBigIntegerExact());
        }
        throw new RulerRuntimeException("round expects a number");
    }
}
