package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.LogicOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.token.TokenType;

public interface OperationDefineManager {

    LogicOperation findLogicOperation(TokenType type);

    Operation findOperation(TokenType type);

    AssignOperation findAssignOperation(TokenType type);

    int findPrecedence(TokenType type);

}
