package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 词法异常
 * 表示词法分析阶段的错误，如非法字符、未闭合的字符串等
 *
 * @author Kamijoucen
 */
public class LexicalException extends RulerException {

    private static final long serialVersionUID = 1L;

    public LexicalException(String message, TokenLocation location) {
        super(ErrorType.LEXICAL_ERROR, message, location);
    }

    public LexicalException(String message, TokenLocation location, Throwable cause) {
        super(ErrorType.LEXICAL_ERROR, message, location, cause);
    }

    /**
     * 未知符号
     */
    public static LexicalException unknownSymbol(char symbol, TokenLocation location) {
        String message = String.format("未知符号 '%c'", symbol);
        return new LexicalException(message, location);
    }

    /**
     * 字符串未闭合
     */
    public static LexicalException unclosedString(TokenLocation location) {
        return new LexicalException("字符串未闭合，缺少结束引号", location);
    }

    /**
     * 非法的转义字符
     */
    public static LexicalException illegalEscape(char escape, TokenLocation location) {
        String message = String.format("非法的转义字符 '\\%c'", escape);
        return new LexicalException(message, location);
    }

    /**
     * 数字格式错误
     */
    public static LexicalException invalidNumber(String number, TokenLocation location) {
        String message = String.format("无效的数字格式 '%s'", number);
        return new LexicalException(message, location);
    }

    /**
     * 非法的标识符
     */
    public static LexicalException illegalIdentifier(String identifier, TokenLocation location) {
        String message = String.format("非法的标识符 '%s'", identifier);
        return new LexicalException(message, location);
    }

    /**
     * 非法的字符
     */
    public static LexicalException illegalCharacter(char ch, TokenLocation location) {
        String message = String.format("非法字符 '%c' (Unicode: U+%04X)", ch, (int) ch);
        return new LexicalException(message, location);
    }
}
