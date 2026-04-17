package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.IllegalOperationException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.util.NumberUtil;

import java.math.BigDecimal;

public abstract class AbstractArithmeticOperation implements BinaryOperation {

    protected abstract BaseValue computeInteger(IntegerValue l, IntegerValue r, RuntimeContext context);

    protected abstract BaseValue computeDecimal(DoubleValue l, DoubleValue r, RuntimeContext context);

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        ValueType lType = lValue.getType();
        ValueType rType = rValue.getType();

        if (lType == ValueType.INTEGER && rType == ValueType.INTEGER) {
            return computeInteger((IntegerValue) lValue, (IntegerValue) rValue, context);
        }

        if ((lType == ValueType.INTEGER || lType == ValueType.DOUBLE)
                && (rType == ValueType.INTEGER || rType == ValueType.DOUBLE)) {
            BigDecimal lv = NumberUtil.toBigDecimal(lValue);
            BigDecimal rv = NumberUtil.toBigDecimal(rValue);
            return computeDecimal(new DoubleValue(lv), new DoubleValue(rv), context);
        }

        throw new IllegalOperationException(
                operationName() + " operation not supported for: " + lType + " and " + rType);
    }

    protected abstract String operationName();
}
