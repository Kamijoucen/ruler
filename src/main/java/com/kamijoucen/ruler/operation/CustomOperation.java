package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.VirtualNode;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Objects;

public class CustomOperation implements BinaryOperation {

    private BinaryOperation callOperation;

    private void init(RuntimeContext context) {
        if (callOperation == null) {
            callOperation = context.getConfiguration().getBinaryOperationFactory()
                    .findOperation(TokenType.CALL.name());
            Objects.requireNonNull(callOperation);
        }
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context, BaseValue... params) {
        init(context);

        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        VirtualNode virtualNode = new VirtualNode(params[0]);
        BaseValue returnValue = callOperation.invoke(virtualNode, null, scope, context, lValue, rValue);
        context.setReturnFlag(false);
        context.clearReturnSpace();
        return returnValue;
    }

}
