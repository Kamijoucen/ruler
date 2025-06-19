package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

/**
 * 一元减法操作（负号）
 * 支持整数和浮点数的取负操作
 *
 * @author Kamijoucen
 */
public class UnarySubOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value = params[0];

        switch (value.getType()) {
            case INTEGER:
                IntegerValue intValue = (IntegerValue) value;
                long negValue = -intValue.getValue();

                // 检查溢出
                if (intValue.getValue() == Long.MIN_VALUE) {
                    // Long.MIN_VALUE的负数会溢出，转换为double
                    return new DoubleValue(-((double) intValue.getValue()));
                }

                return context.getConfiguration().getIntegerNumberCache().getValue(negValue);

            case DOUBLE:
                DoubleValue doubleValue = (DoubleValue) value;
                return new DoubleValue(-doubleValue.getValue());

            default:
                throw TypeException.unsupportedOperation("-", value.getType(), null);
        }
    }
}
