package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.IllegalOperationException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public abstract class AbstractArithmeticOperation implements BinaryOperation {

    protected abstract BaseValue computeLong(long l, long r, RuntimeContext context);

    protected abstract BaseValue computeDouble(double l, double r, RuntimeContext context);

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        ValueType lType = lValue.getType();
        ValueType rType = rValue.getType();

        if (lType == ValueType.INTEGER && rType == ValueType.INTEGER) {
            return computeLong(
                    ((IntegerValue) lValue).getValue(),
                    ((IntegerValue) rValue).getValue(),
                    context);
        }

        if ((lType == ValueType.INTEGER || lType == ValueType.DOUBLE)
                && (rType == ValueType.INTEGER || rType == ValueType.DOUBLE)) {
            double lv = lType == ValueType.INTEGER
                    ? ((IntegerValue) lValue).getValue()
                    : ((DoubleValue) lValue).getValue();
            double rv = rType == ValueType.INTEGER
                    ? ((IntegerValue) rValue).getValue()
                    : ((DoubleValue) rValue).getValue();
            return computeDouble(lv, rv, context);
        }

        throw new IllegalOperationException(
                operationName() + " operation not supported for: " + lType + " and " + rType);
    }

    protected abstract String operationName();
}
