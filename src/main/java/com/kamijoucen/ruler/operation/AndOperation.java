package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * 逻辑与操作
 * 支持短路求值
 *
 * @author Kamijoucen
 */
public class AndOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);

        // 类型检查
        if (lValue.getType() != ValueType.BOOL) {
            throw TypeException.unsupportedOperation("&&", lValue.getType(), lhs.getLocation());
        }

        // 短路求值：如果左值为false，直接返回false
        if (!((BoolValue) lValue).getValue()) {
            return BoolValue.FALSE;
        }

        // 计算右值
        BaseValue rValue = rhs.eval(scope, context);

        // 类型检查
        if (rValue.getType() != ValueType.BOOL) {
            throw TypeException.unsupportedOperation("&&", rValue.getType(), rhs.getLocation());
        }

        return ((BoolValue) rValue).getValue() ? BoolValue.TRUE : BoolValue.FALSE;
    }
}
