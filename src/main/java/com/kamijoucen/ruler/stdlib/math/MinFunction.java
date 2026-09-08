package com.kamijoucen.ruler.stdlib.math;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DoubleValue;
import com.kamijoucen.ruler.types.value.IntegerValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.logic.util.NumberUtil;


public class MinFunction implements RulerFunction {

    @Override
    public String getName() {
        return "min";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        BaseValue min = (BaseValue) param[0];
        boolean hasDouble = min.getType() == ValueType.DOUBLE;
        for (int i = 1; i < param.length; i++) {
            BaseValue value = (BaseValue) param[i];
            if (value.getType() == ValueType.DOUBLE) {
                hasDouble = true;
            }
            if (NumberUtil.compareNumbers(value, min) < 0) {
                min = value;
            }
        }
        if (hasDouble) {
            return new DoubleValue(NumberUtil.toBigDecimal(min));
        }
        if (min.getType() == ValueType.INTEGER) {
            return IntegerValue.valueOf(((IntegerValue) min).getValue());
        }
        return new DoubleValue(NumberUtil.toBigDecimal(min));
    }
}
