package com.kamijoucen.ruler.token;

import java.util.HashMap;
import java.util.Map;

public enum TokenLookUp {

    INSTANCE;

    TokenLookUp() {
        map.put(".", TokenType.DOT);
        map.put("(", TokenType.LEFT_PAREN);
        map.put(")", TokenType.RIGHT_PAREN);
        map.put("<", TokenType.LT);
        map.put(">", TokenType.GT);
        map.put("<=", TokenType.LE);
        map.put(">=", TokenType.GE);
        map.put("=", TokenType.ASSIGN);
        map.put("==", TokenType.EQ);
        map.put("!=", TokenType.NE);
        map.put("+", TokenType.ADD);
        map.put("-", TokenType.SUB);
        map.put("*", TokenType.MUL);
        map.put("/", TokenType.DIV);
        map.put("return", TokenType.KEY_RETURN);
        map.put("def", TokenType.KEY_DEF);
        map.put("if", TokenType.KEY_IF);
        map.put("for", TokenType.KEY_FOR);
        map.put("break", TokenType.KEY_BREAK);
        map.put("continue", TokenType.KEY_CONTINUE);
        map.put("list", TokenType.KEY_LIST);
        map.put("map", TokenType.KEY_MAP);
        map.put("false", TokenType.KEY_FALSE);
        map.put("true", TokenType.KEY_TRUE);
    }

    private final Map<String, TokenType> map = new HashMap<String, TokenType>();

    public TokenType lookup(String str) {
        TokenType type = map.get(str);
        if (type != null) {
            return type;
        }
        return TokenType.UN_KNOW;
    }

}
