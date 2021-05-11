package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.basic.*;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class BinaryDefine {

    private static final Map<TokenType, Integer> PRECEDENCE = new HashMap<TokenType, Integer>();

    private static final Map<TokenType, Operation> OPERATION = new HashMap<TokenType, Operation>();

    static {

        PRECEDENCE.put(TokenType.EQ, 10);       // ==
        PRECEDENCE.put(TokenType.NE, 10);       // !=
        PRECEDENCE.put(TokenType.LT, 20);       // <
        PRECEDENCE.put(TokenType.GT, 20);       // >
        PRECEDENCE.put(TokenType.LE, 20);       // <=
        PRECEDENCE.put(TokenType.GE, 20);       // >=
        PRECEDENCE.put(TokenType.ADD, 30);      // +
        PRECEDENCE.put(TokenType.SUB, 30);      // -
        PRECEDENCE.put(TokenType.MUL, 40);      // *
        PRECEDENCE.put(TokenType.DIV, 40);      // /

//        OPERATION.put(TokenType.EQ, 10);       // ==
//        OPERATION.put(TokenType.NE, 10);       // !=
//        OPERATION.put(TokenType.LT, 20);       // <
//        OPERATION.put(TokenType.GT, 20);       // >
//        OPERATION.put(TokenType.LE, 20);       // <=
//        OPERATION.put(TokenType.GE, 20);       // >=
        OPERATION.put(TokenType.ADD, new AddOperation());      // +
        OPERATION.put(TokenType.SUB, new SubOperation());      // -
        OPERATION.put(TokenType.MUL, new MulOperation());      // *
        OPERATION.put(TokenType.DIV, new DivOperation());      // /

    }

    public static Operation findOperation(TokenType type) {
        Operation operation = OPERATION.get(type);
        if (operation == null) {
            throw SyntaxException.withSyntax("不支持的操作符:" + type);
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
