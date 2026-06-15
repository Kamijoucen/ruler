package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.ImportNode;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;

/**
 * Capability interface required by parser logic.
 */
public interface ParserManager {

    ImportNode parseImport();

    BaseNode parseStatement();

    BaseNode parseExpression();

    BaseNode parseExpression(String expression, TokenLocation location);

    BaseNode parseBinaryNode(int expPrec, BaseNode lhs);

    BaseNode parsePrimaryExpression();

    BinaryOperation findOperation(String operationName);

    TokenStream getTokenStream();

    boolean isInLoop();

    void setInLoop(boolean inLoop);
}
