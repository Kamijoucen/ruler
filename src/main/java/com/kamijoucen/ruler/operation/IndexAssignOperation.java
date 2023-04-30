package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;


public class IndexAssignOperation implements AssignOperation {

    @Override
    public BaseValue assign(BaseValue preOperationValue, OperationNode call, BaseNode expression, Scope scope, RuntimeContext context) {
        if (preOperationValue.getType() != ValueType.ARRAY) {
            throw SyntaxException.withSyntax(preOperationValue.getType() + "不是一个数组");
        }
        ArrayValue arrayValue = (ArrayValue) preOperationValue;
        BaseValue tempIndexValue = ((IndexNode) call).getIndex().eval(context, scope);
        if (tempIndexValue.getType() != ValueType.INTEGER) {
            throw SyntaxException.withSyntax("数组的索引必须是数字");
        }
        IntegerValue indexValue = (IntegerValue) tempIndexValue;
        BaseValue value = expression.eval(scope, context);
        arrayValue.getValues().set((int) indexValue.getValue(), value);
        return value;
    }
}
