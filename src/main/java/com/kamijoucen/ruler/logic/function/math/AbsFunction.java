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
import java.math.BigInteger;

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
            BigInteger v = ((IntegerValue) value).getValue();
            return context.getConfiguration().getIntegerNumberCache().getValue(v.abs());
        } else if (value.getType() == ValueType.DOUBLE) {
            BigDecimal v = ((DoubleValue) value).getValue();
            return new DoubleValue(v.abs());
        }
        throw new RulerRuntimeException("abs expects a number");
    }
}
