package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

public class IndexOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {

        BaseValue lValue = lhs.eval(scope, context);
        BaseValue indexValue = rhs.eval(scope, context);

        if (indexValue.getType() != ValueType.INTEGER) {
            throw SyntaxException.withSyntax("数组的索引必须是数字");
        }
        ArrayValue array = (ArrayValue) lValue;
        IntegerValue index = (IntegerValue) indexValue;
        if (index.getValue() >= array.getValues().size()) {
            // todo 是否允许数组越界
            return NullValue.INSTANCE;
        }
        return array.getValues().get((int) index.getValue());
    }

}
