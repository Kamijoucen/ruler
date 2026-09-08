package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.exception.IllegalOperationException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.logic.util.NumberUtil;

public abstract class AbstractCompareOperation implements BinaryOperation {

    protected abstract boolean compareNumber(int cmpResult);

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = EvalVisitor.evaluate(lhs, scope, context);
        BaseValue rValue = EvalVisitor.evaluate(rhs, scope, context);

        ValueType lType = lValue.getType();
        ValueType rType = rValue.getType();

        if ((lType == ValueType.INTEGER || lType == ValueType.DOUBLE)
                && (rType == ValueType.INTEGER || rType == ValueType.DOUBLE)) {
            return BoolValue.get(compareNumber(NumberUtil.compareNumbers(lValue, rValue)));
        }

        if (lType == ValueType.STRING && rType == ValueType.STRING) {
            int cmp = ((StringValue) lValue).getValue().compareTo(((StringValue) rValue).getValue());
            return BoolValue.get(compareNumber(cmp));
        }

        throw new IllegalOperationException(
                operationName() + " operation not supported for: " + lType + " and " + rType);
    }

    protected abstract String operationName();
}
