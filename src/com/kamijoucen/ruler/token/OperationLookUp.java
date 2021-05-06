package com.kamijoucen.ruler.token;

import java.util.HashMap;
import java.util.Map;

public class OperationLookUp {

    private static final Map<TokenType, Integer> map = new HashMap<TokenType, Integer>();

    static {

        map.put(TokenType.EQ, 10);       // ==
        map.put(TokenType.NE, 10);       // !=

        map.put(TokenType.LT, 20);       // <
        map.put(TokenType.GT, 20);       // >
        map.put(TokenType.LE, 20);       // <=
        map.put(TokenType.GE, 20);       // >=

        map.put(TokenType.ADD, 30);      // +
        map.put(TokenType.SUB, 30);      // -

        map.put(TokenType.MUL, 40);      // *
        map.put(TokenType.DIV, 40);      // /

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
