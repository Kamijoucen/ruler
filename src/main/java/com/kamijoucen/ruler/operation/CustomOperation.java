package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.VirtualNode;
import com.kamijoucen.ruler.common.NodeVisitor;
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
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context,
            NodeVisitor visitor, BaseValue... params) {
        init(context);

        BaseValue lValue = lhs.eval(visitor);
        BaseValue rValue = rhs.eval(visitor);

        VirtualNode virtualNode = new VirtualNode(params[0]);
        BaseValue returnValue =
                callOperation.invoke(virtualNode, null, env, context, visitor, lValue, rValue);
        context.setReturnFlag(false);
        context.clearReturnSpace();
        return returnValue;
    }

}
