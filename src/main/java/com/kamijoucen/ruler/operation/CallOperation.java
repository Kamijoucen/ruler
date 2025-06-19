package com.kamijoucen.ruler.operation;

import java.util.Arrays;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.FunctionValue;

/**
 * 函数调用操作
 * 支持普通函数和闭包的调用
 *
 * @author Kamijoucen
 */
public class CallOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue callFunc = lhs.eval(scope, context);
        BaseValue self = context.getCurrentSelfValue();

        // 检查栈深度
        context.getStackDepthCheckOperation().pushCallStack(context, lhs.getLocation());
        try {
            BaseValue[] funcParam = Arrays.copyOfRange(params, 0, params.length);

            switch (callFunc.getType()) {
                case FUNCTION:
                    RulerFunction function = ((FunctionValue) callFunc).getValue();
                    return (BaseValue) function.call(context, scope, self, (Object[]) funcParam);

                case CLOSURE:
                    ClosureValue closureFunction = ((ClosureValue) callFunc);
                    return context.getConfiguration().getCallClosureExecutor().call(self,
                            closureFunction, scope, context, funcParam);

                default:
                    // 不是可调用的类型
                    throw TypeException.notCallable(callFunc.getType(), lhs.getLocation());
            }
        } finally {
            context.getStackDepthCheckOperation().popCallStack();
        }
    }
}
