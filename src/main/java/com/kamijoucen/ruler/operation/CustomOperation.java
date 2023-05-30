package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.VirtualNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Objects;

public class CustomOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BinaryOperation callOperation = context.getConfiguration()
                .getBinaryOperationFactory().findOperation(TokenType.CALL.name());
        Objects.requireNonNull(callOperation);

        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        VirtualNode virtualNode = new VirtualNode(params[0]);
        return callOperation.invoke(virtualNode, null, scope, context, lValue, rValue);
    }

}
