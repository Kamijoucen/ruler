package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public interface OperationNode extends BaseNode {

    TokenType getOperationType();

    void putOperation(Operation operation);

    void putOperationValue(BaseValue value);

}
