package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;
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
            return context.getConfiguration().getIntegerNumberCache().getValue(((IntegerValue) min).getValue());
        }
        return new DoubleValue(NumberUtil.toBigDecimal(min));
    }
}
