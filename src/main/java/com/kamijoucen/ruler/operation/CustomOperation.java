package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.*;

public class CustomOperation implements Operation {

    private static final Operation CALL_OPERATION = OperationDefine.findOperation(TokenType.CALL);

    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {
        if (param.length != 3) {
            // 自定义操作符需要2个参数和1个函数值本身，因此这里检查参数是否是3个
            throw new RuntimeException("The custom operator takes two arguments");
        }
        return CALL_OPERATION.compute(context, param);
    }

}
