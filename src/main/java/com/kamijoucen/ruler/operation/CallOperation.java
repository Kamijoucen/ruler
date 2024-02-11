package com.kamijoucen.ruler.operation;

import java.util.Arrays;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.FunctionValue;

public class CallOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue callFunc = lhs.eval(scope, context);
        BaseValue self = context.getCurrentSelfValue();
        context.getStackDepthCheckOperation().addDepth(context);
        try {
            switch (callFunc.getType()) {
                case FUNCTION:
                    RulerFunction function = ((FunctionValue) callFunc).getValue();
                    return (BaseValue) function.call(context, scope, self, (Object[]) params);
                case CLOSURE:
                    ClosureValue closureFunction = ((ClosureValue) callFunc);
                    return context.getConfiguration().getCallClosureExecutor().call(self,
                            closureFunction, scope, context, params);
                default: {
                    Object printObj = callFunc;
                    if (lhs instanceof DotNode) {
                        printObj = ((NameNode) ((DotNode) lhs).getRhs()).name.name;
                    }
                    // TODO 优化错误信息
                    throw new IllegalArgumentException(printObj + " not is a function!");
                }
            }
        } finally {
            context.getStackDepthCheckOperation().subDepth(context);
        }
    }

}
