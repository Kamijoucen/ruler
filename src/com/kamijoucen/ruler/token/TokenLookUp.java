package com.kamijoucen.ruler.token;

import java.util.HashMap;
import java.util.Map;

public class TokenLookUp {

    private static final Map<String, TokenType> KEY_WORDS = new HashMap<String, TokenType>();

    private static final Map<String, TokenType> SYMBOL = new HashMap<String, TokenType>();

    static {
        SYMBOL.put(",", TokenType.COMMA);
        SYMBOL.put(";", TokenType.SEMICOLON);
        SYMBOL.put(".", TokenType.DOT);
        SYMBOL.put("(", TokenType.LEFT_PAREN);
        SYMBOL.put(")", TokenType.RIGHT_PAREN);
        SYMBOL.put("<", TokenType.LT);
        SYMBOL.put(">", TokenType.GT);
        SYMBOL.put("<=", TokenType.LE);
        SYMBOL.put(">=", TokenType.GE);
        SYMBOL.put("=", TokenType.ASSIGN);
        SYMBOL.put("==", TokenType.EQ);
        SYMBOL.put("!=", TokenType.NE);
        SYMBOL.put("+", TokenType.ADD);
        SYMBOL.put("-", TokenType.SUB);
        SYMBOL.put("*", TokenType.MUL);
        SYMBOL.put("/", TokenType.DIV);

        KEY_WORDS.put("return", TokenType.KEY_RETURN);
        KEY_WORDS.put("def", TokenType.KEY_DEF);
        KEY_WORDS.put("if", TokenType.KEY_IF);
        KEY_WORDS.put("for", TokenType.KEY_FOR);
        KEY_WORDS.put("break", TokenType.KEY_BREAK);
        KEY_WORDS.put("continue", TokenType.KEY_CONTINUE);
        KEY_WORDS.put("list", TokenType.KEY_LIST);
        KEY_WORDS.put("map", TokenType.KEY_MAP);
        KEY_WORDS.put("false", TokenType.KEY_FALSE);
        KEY_WORDS.put("true", TokenType.KEY_TRUE);
    }

    public static TokenType symbol(String str) {
        TokenType type = SYMBOL.get(str);
        if (type != null) {
            return type;
        }
        return TokenType.UN_KNOW;
    }

    public static TokenType keyWords(String str) {
        TokenType type = KEY_WORDS.get(str);
        if (type != null) {
            return type;
        }
        return TokenType.UN_KNOW;
    }

}
