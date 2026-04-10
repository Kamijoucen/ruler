package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.IllegalOperationException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public abstract class AbstractCompareOperation implements BinaryOperation {

    protected abstract boolean compareLong(long l, long r);

    protected abstract boolean compareDouble(double l, double r);

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        ValueType lType = lValue.getType();
        ValueType rType = rValue.getType();

        if (lType == ValueType.INTEGER && rType == ValueType.INTEGER) {
            return BoolValue.get(compareLong(
                    ((IntegerValue) lValue).getValue(),
                    ((IntegerValue) rValue).getValue()));
        }

        if ((lType == ValueType.INTEGER || lType == ValueType.DOUBLE)
                && (rType == ValueType.INTEGER || rType == ValueType.DOUBLE)) {
            double lv = lType == ValueType.INTEGER
                    ? ((IntegerValue) lValue).getValue()
                    : ((DoubleValue) lValue).getValue();
            double rv = rType == ValueType.INTEGER
                    ? ((IntegerValue) rValue).getValue()
                    : ((DoubleValue) rValue).getValue();
            return BoolValue.get(compareDouble(lv, rv));
        }

        throw new IllegalOperationException(
                operationName() + " operation not supported for: " + lType + " and " + rType);
    }

    protected abstract String operationName();
}
