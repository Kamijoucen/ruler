package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class CustomOperation implements BinaryOperation {

    private static final BinaryOperation CALL_OPERATION = OperationDefine.findOperation(TokenType.CALL);

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        if (params.length != 3) {
            // 自定义操作符需要2个参数和1个函数值本身，因此这里检查参数是否是3个
            throw new RuntimeException("The custom operator need three arguments");
        }
        return CALL_OPERATION.invoke(context, , param);
    }

}
