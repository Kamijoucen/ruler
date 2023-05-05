package com.kamijoucen.ruler.common;

import java.util.HashMap;
import java.util.Map;

import com.kamijoucen.ruler.token.TokenType;

public class OperationDefine {

    private static final Map<TokenType, Integer> PRECEDENCE = new HashMap<>();

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
