package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.*;
import com.kamijoucen.ruler.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class OperationDefine {

    private static final Map<TokenType, Integer> PRECEDENCE = new HashMap<>();
    private static final Map<TokenType, BinaryOperation> OPERATION = new HashMap<>();
    private static final Map<TokenType, AssignOperation> ASSIGN_OPERATION = new HashMap<>();

    static {

        PRECEDENCE.put(TokenType.OR, 8); // ||
        PRECEDENCE.put(TokenType.AND, 9); // &&
        PRECEDENCE.put(TokenType.EQ, 10); // ==
        PRECEDENCE.put(TokenType.NE, 10); // !=
        PRECEDENCE.put(TokenType.LT, 20); // <
        PRECEDENCE.put(TokenType.GT, 20); // >
        PRECEDENCE.put(TokenType.LE, 20); // <=
        PRECEDENCE.put(TokenType.GE, 20); // >=
        PRECEDENCE.put(TokenType.ADD, 30); // +
        PRECEDENCE.put(TokenType.SUB, 30); // -
        PRECEDENCE.put(TokenType.MUL, 40); // *
        PRECEDENCE.put(TokenType.DIV, 40); // /
        PRECEDENCE.put(TokenType.IDENTIFIER, 30); // custom

        ASSIGN_OPERATION.put(TokenType.INDEX, new IndexAssignOperation()); // []
        ASSIGN_OPERATION.put(TokenType.DOT, new DotAssignOperation()); // .
    }

    public static BinaryOperation findOperation(TokenType type) {
        BinaryOperation operation = OPERATION.get(type);
        if (operation == null) {
            throw new UnsupportedOperationException("Unsupported operator:" + type);
        }
        return operation;
    }

    public static AssignOperation findAssignOperation(TokenType type) {
        AssignOperation operation = ASSIGN_OPERATION.get(type);
        if (operation == null) {
            throw SyntaxException.withSyntax("不支持的赋值操作:" + type);
        }
        return operation;
    }

    public static int findPrecedence(TokenType type) {
        Integer precedence = PRECEDENCE.get(type);
        if (precedence == null) {
            return -1;
        } else {
            return precedence;
        }
    }
}
