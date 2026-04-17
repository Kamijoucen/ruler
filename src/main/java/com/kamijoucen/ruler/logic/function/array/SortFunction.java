package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.Collections;
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
                return Double.compare(toDouble(a), toDouble(b));
            }
            if (a.getType() == ValueType.STRING && b.getType() == ValueType.STRING) {
                return ((StringValue) a).getValue().compareTo(((StringValue) b).getValue());
            }
            throw new RulerRuntimeException("arraySort default comparator only supports numbers and strings");
        };
    }

    private Comparator<BaseValue> customComparator(BaseValue comparator, Scope scope, RuntimeContext context) {
        return (a, b) -> {
            BaseValue result;
            if (comparator.getType() == ValueType.CLOSURE) {
                result = context.getConfiguration().getCallClosureExecutor()
                        .call((ClosureValue) comparator, scope, context, a, b);
            } else if (comparator.getType() == ValueType.FUNCTION) {
                result = (BaseValue) ((FunctionValue) comparator).getValue()
                        .call(context, scope, null, a, b);
            } else {
                throw new RulerRuntimeException("arraySort comparator must be a function");
            }
            if (result.getType() == ValueType.INTEGER) {
                long v = ((IntegerValue) result).getValue();
                return v > 0 ? 1 : (v < 0 ? -1 : 0);
            } else if (result.getType() == ValueType.DOUBLE) {
                double v = ((DoubleValue) result).getValue();
                return v > 0 ? 1 : (v < 0 ? -1 : 0);
            }
            throw new RulerRuntimeException("arraySort comparator must return a number");
        };
    }

    private boolean isNumber(BaseValue value) {
        return value.getType() == ValueType.INTEGER || value.getType() == ValueType.DOUBLE;
    }

    private double toDouble(BaseValue value) {
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue();
        }
        return ((DoubleValue) value).getValue();
    }
}
