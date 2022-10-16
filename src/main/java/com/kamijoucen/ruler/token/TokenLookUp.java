package com.kamijoucen.ruler.token;

import java.util.HashMap;
import java.util.Map;

public class TokenLookUp {

    private static final Map<String, TokenType> KEY_WORDS = new HashMap<String, TokenType>();

    private static final Map<String, TokenType> SYMBOL = new HashMap<String, TokenType>();

    static {
        SYMBOL.put(",", TokenType.COMMA);
        SYMBOL.put(";", TokenType.SEMICOLON);
        SYMBOL.put(":", TokenType.COLON);
        SYMBOL.put(".", TokenType.DOT);
        SYMBOL.put("(", TokenType.LEFT_PAREN);
        SYMBOL.put(")", TokenType.RIGHT_PAREN);
        SYMBOL.put("{", TokenType.LEFT_BRACE);
        SYMBOL.put("}", TokenType.RIGHT_BRACE);
        SYMBOL.put("[", TokenType.LEFT_SQUARE);
        SYMBOL.put("]", TokenType.RIGHT_SQUARE);
        SYMBOL.put("<", TokenType.LT);
        SYMBOL.put(">", TokenType.GT);
        SYMBOL.put("<=", TokenType.LE);
        SYMBOL.put(">=", TokenType.GE);
        SYMBOL.put("=", TokenType.ASSIGN);
        SYMBOL.put("==", TokenType.EQ);
        SYMBOL.put("!=", TokenType.NE);
        SYMBOL.put("&&", TokenType.AND);
        SYMBOL.put("||", TokenType.OR);
        SYMBOL.put("->", TokenType.ARROW);
        SYMBOL.put("!", TokenType.NOT);
        SYMBOL.put("+", TokenType.ADD);
        SYMBOL.put("-", TokenType.SUB);
        SYMBOL.put("*", TokenType.MUL);
        SYMBOL.put("/", TokenType.DIV);

        KEY_WORDS.put("return", TokenType.KEY_RETURN);
        KEY_WORDS.put("if", TokenType.KEY_IF);
        KEY_WORDS.put("for", TokenType.KEY_FOR);
        KEY_WORDS.put("while", TokenType.KEY_WHILE);
        KEY_WORDS.put("break", TokenType.KEY_BREAK);
        KEY_WORDS.put("continue", TokenType.KEY_CONTINUE);
        KEY_WORDS.put("false", TokenType.KEY_FALSE);
        KEY_WORDS.put("true", TokenType.KEY_TRUE);
        KEY_WORDS.put("else", TokenType.KEY_ELSE);
        KEY_WORDS.put("fun", TokenType.KEY_FUN);
        KEY_WORDS.put("var", TokenType.KEY_VAR);
        KEY_WORDS.put("null", TokenType.KEY_NULL);
        KEY_WORDS.put("this", TokenType.KEY_THIS);
        KEY_WORDS.put("import", TokenType.KEY_IMPORT);
        KEY_WORDS.put("init", TokenType.KEY_INIT);
        KEY_WORDS.put("typeof", TokenType.KEY_TYPEOF);
        KEY_WORDS.put("in", TokenType.KEY_IN);
        KEY_WORDS.put("rule", TokenType.KEY_RULE);
        KEY_WORDS.put("infix", TokenType.KEY_INFIX);
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
