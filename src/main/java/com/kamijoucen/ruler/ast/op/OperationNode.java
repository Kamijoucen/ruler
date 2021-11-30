package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;

public abstract class OperationNode extends AbstractBaseNode {

    public abstract TokenType getOperationType();

    public abstract void putOperation(Operation operation);

    public abstract void putAssignOperation(AssignOperation operation);

    public abstract void assign(BaseNode expression, Scope scope, RuntimeContext context);

}
