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
            if (callFunc.getType() == ValueType.FUNCTION) {
                RulerFunction function = ((FunctionValue) callFunc).getValue();
                return (BaseValue) function.call(context, self, funcParam);
            } else if (callFunc.getType() == ValueType.CLOSURE) {
                ClosureValue function = ((ClosureValue) callFunc);
                return context.getConfiguration().getCallClosureExecutor()
                        .call(self, function, scope, context, params);
            } else {
                throw new IllegalArgumentException(callFunc + " not is a function!");
            }
        } finally {
            context.getStackDepthCheckOperation().subDepth(context);
        }
    }

}
