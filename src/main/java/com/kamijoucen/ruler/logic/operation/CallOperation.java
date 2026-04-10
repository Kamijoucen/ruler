package com.kamijoucen.ruler.logic.operation;

import java.util.Arrays;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.DotNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import com.kamijoucen.ruler.domain.value.FunctionValue;
import com.kamijoucen.ruler.domain.value.MethodValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class CallOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue callFunc = lhs.eval(scope, context);
        BaseValue[] funcParam = Arrays.copyOfRange(params, 0, params.length);
        BaseValue boundSelf = null;
        if (callFunc.getType() == ValueType.METHOD) {
            MethodValue method = (MethodValue) callFunc;
            boundSelf = method.getBoundSelf();
            callFunc = method.getTarget();
            // 只有闭包需要把 boundSelf 前置到参数列表；内建函数通过 self 参数接收调用者
            if (callFunc instanceof ClosureValue) {
                BaseValue[] newParams = new BaseValue[funcParam.length + 1];
                newParams[0] = boundSelf;
                System.arraycopy(funcParam, 0, newParams, 1, funcParam.length);
                funcParam = newParams;
            }
        }
        context.getStackDepthCheckOperation().addDepth(context);
        try {
            switch (callFunc.getType()) {
                case FUNCTION:
                    RulerFunction function = ((FunctionValue) callFunc).getValue();
                    return (BaseValue) function.call(context, scope, boundSelf, (Object[]) funcParam);
                case CLOSURE:
                    ClosureValue closureFunction = ((ClosureValue) callFunc);
                    return context.getConfiguration().getCallClosureExecutor().call(
                            closureFunction, scope, context, funcParam);
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
