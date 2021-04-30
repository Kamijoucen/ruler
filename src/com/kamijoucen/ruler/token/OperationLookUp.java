package com.kamijoucen.ruler.token;

import java.util.HashMap;
import java.util.Map;

public class OperationLookUp {

    private static final Map<TokenType, Integer> map = new HashMap<TokenType, Integer>();

    static {

        map.put(TokenType.LT, 1);       // <
        map.put(TokenType.GT, 1);       // >
        map.put(TokenType.LE, 1);       // <=
        map.put(TokenType.GE, 1);       // >=
        map.put(TokenType.EQ, 1);       // ==
        map.put(TokenType.NE, 1);       // !=
        map.put(TokenType.ADD, 1);      // +
        map.put(TokenType.SUB, 1);      // -
        map.put(TokenType.MUL, 1);      // *
        map.put(TokenType.DIV, 1);      // /

    }

    public static int lookUp(TokenType type) {
        Integer precedence = map.get(type);
        if (precedence == null) {
            return -1;
        } else {
            return precedence;
        }
    }
}
