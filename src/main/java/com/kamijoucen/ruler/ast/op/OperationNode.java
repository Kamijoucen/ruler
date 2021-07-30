package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;

public interface OperationNode extends BaseNode {

    TokenType getOperationType();

    void putOperation(Operation operation);

    void putAssignOperation(AssignOperation operation);

    void assign(BaseNode expression, Scope scope);

}
