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

public class UnarySubOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value = params[0];
        ValueType type = value.getType();
        if (type == ValueType.INTEGER) {
            return NumberUtil.negate((IntegerValue) value);
        }
        if (type == ValueType.DOUBLE) {
            return NumberUtil.negate((DoubleValue) value);
        }
        throw new IllegalOperationException(
                "Negation operation is not supported for this value: " + value);
    }
}
