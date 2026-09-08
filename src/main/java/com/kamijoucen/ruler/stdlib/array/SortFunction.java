package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.*;
import com.kamijoucen.ruler.logic.eval.CallLogic;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.logic.util.NumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

public class SortFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arraySort";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arraySort expects an array");
        }
        List<BaseValue> values = arr.getValues();
        BaseValue comparator = param.length >= off + 1 ? (BaseValue) param[off] : null;

        if (comparator == null || comparator.getType() == ValueType.NULL) {
            values.sort(defaultComparator());
        } else {
            values.sort(customComparator(comparator, currentScope, context));
        }
        return arr;
    }

    private Comparator<BaseValue> defaultComparator() {
        return (a, b) -> {
            if (isNumber(a) && isNumber(b)) {
                return NumberUtil.compareNumbers(a, b);
            }
            if (a.getType() == ValueType.STRING && b.getType() == ValueType.STRING) {
                return ((StringValue) a).getValue().compareTo(((StringValue) b).getValue());
            }
            throw new RulerRuntimeException("arraySort default comparator only supports numbers and strings");
        };
    }

    private Comparator<BaseValue> customComparator(BaseValue comparator, Scope scope, RuntimeContext context) {
        return (a, b) -> {
            BaseValue result = CallLogic.callValue(comparator, scope, context, a, b);
            if (result.getType() == ValueType.INTEGER) {
                BigInteger v = ((IntegerValue) result).getValue();
                return v.compareTo(BigInteger.ZERO);
            } else if (result.getType() == ValueType.DOUBLE) {
                BigDecimal v = ((DoubleValue) result).getValue();
                return v.compareTo(BigDecimal.ZERO);
            }
            throw new RulerRuntimeException("arraySort comparator must return a number");
        };
    }

    private boolean isNumber(BaseValue value) {
        return value.getType() == ValueType.INTEGER || value.getType() == ValueType.DOUBLE;
    }
}
