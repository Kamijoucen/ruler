package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;

public class CallOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue callFunc = lhs.eval(scope, context);
        BaseValue self = context.getCurrentSelfValue();
        context.getStackDepthCheckOperation().addDepth(context);
        try {
            Object[] funcParam = Arrays.copyOfRange(params, 0, params.length);
            switch (callFunc.getType()) {
                case FUNCTION:
                    RulerFunction function = ((FunctionValue) callFunc).getValue();
                    return (BaseValue) function.call(context, scope, self, funcParam);
                case CLOSURE:
                    ClosureValue closureFunction = ((ClosureValue) callFunc);
                    return context.getConfiguration().getCallClosureExecutor()
                            .call(self, closureFunction, scope, context, params);
                default:
                    throw new IllegalArgumentException(callFunc + " not is a function!");
            }
        } finally {
            context.getStackDepthCheckOperation().subDepth(context);
        }
    }

}
