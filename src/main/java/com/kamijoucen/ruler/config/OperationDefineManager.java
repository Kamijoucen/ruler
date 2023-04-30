package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.LogicOperation;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.token.TokenType;

public interface OperationDefineManager {

    LogicOperation findLogicOperation(TokenType type);

    BinaryOperation findOperation(TokenType type);

    AssignOperation findAssignOperation(TokenType type);

    int findPrecedence(TokenType type);

}
