package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
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
        return callOperation.invoke(lhs, null, scope, context, null);
    }

}
