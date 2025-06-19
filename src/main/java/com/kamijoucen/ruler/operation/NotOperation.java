package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * 逻辑非操作
 * 注意：虽然实现了BinaryOperation接口，但实际上是一元操作
 *
 * @author Kamijoucen
 */
public class NotOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value;

        // 处理两种调用方式：
        // 1. 从UnaryOperationEval调用时，params[0]包含操作数
        // 2. 从BinaryOperationEval调用时，params为空，需要从lhs获取操作数
        if (params != null && params.length > 0) {
            value = params[0];
        } else {
            // 对于一元操作，操作数在lhs中
            value = lhs.eval(scope, context);
        }

        if (value.getType() != ValueType.BOOL) {
            throw TypeException.unsupportedOperation("!", value.getType(), null);
        }

        BoolValue boolValue = (BoolValue) value;
        return boolValue.getValue() ? BoolValue.FALSE : BoolValue.TRUE;
    }
}
