package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public abstract class OperationNode extends AbstractBaseNode {

    public OperationNode(TokenLocation location) {
        super(location);
    }

    public abstract TokenType getOperationType();

    public abstract void putOperation(BinaryOperation operation);

    public abstract void putAssignOperation(AssignOperation operation);

    public abstract BaseValue assign(BaseNode expression, Scope scope, RuntimeContext context);

}
