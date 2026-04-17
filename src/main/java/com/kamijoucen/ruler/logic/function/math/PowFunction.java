package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;

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
        return new DoubleValue(Math.pow(base, exponent));
    }

    private double toDouble(BaseValue value) {
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue();
        } else if (value.getType() == ValueType.DOUBLE) {
            return ((DoubleValue) value).getValue();
        }
        throw new RulerRuntimeException("pow expects numbers");
    }
}
