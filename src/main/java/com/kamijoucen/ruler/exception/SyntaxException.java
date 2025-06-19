package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 语法异常
 * 表示脚本中的语法错误，如缺少分号、括号不匹配等
 *
 * @author Kamijoucen
 */
public class SyntaxException extends RulerException {

    private static final long serialVersionUID = 1L;

    /**
     * 创建语法异常
     *
     * @param message 错误信息
     * @param location 错误位置
     */
    public SyntaxException(String message, TokenLocation location) {
        super(ErrorType.SYNTAX_ERROR, message, location);
    }

    /**
     * 创建语法异常（带原因）
     *
     * @param message 错误信息
     * @param location 错误位置
     * @param cause 原因
     */
    public SyntaxException(String message, TokenLocation location, Throwable cause) {
        super(ErrorType.SYNTAX_ERROR, message, location, cause);
    }

    /**
     * 创建语法异常（兼容旧代码）
     *
     * @param message 错误信息
     */
    public SyntaxException(String message) {
        super(ErrorType.SYNTAX_ERROR, message, null);
    }

    // 以下是静态工厂方法，用于创建常见的语法错误

    /**
     * 创建带语法错误前缀的异常（兼容旧代码）
     *
     * @param msg 错误描述
     * @return 语法异常实例
     */
    public static SyntaxException withSyntax(String msg) {
        return new SyntaxException(msg, null);
    }

    /**
     * 创建带语法错误前缀和token信息的异常（兼容旧代码）
     *
     * @param msg 错误描述
     * @param token 相关的token
     * @return 语法异常实例
     */
    public static SyntaxException withSyntax(String msg, Token token) {
        TokenLocation location = token != null ? token.location : null;
        return new SyntaxException(msg, location);
    }

    /**
     * 创建带语法错误前缀和位置信息的异常（兼容旧代码）
     *
     * @param msg 错误描述
     * @param location 错误位置
     * @return 语法异常实例
     */
    public static SyntaxException withSyntax(String msg, TokenLocation location) {
        return new SyntaxException(msg, location);
    }

    // 新的静态工厂方法，提供更清晰的错误信息

    /**
     * 期望特定的符号但遇到了其他符号
     */
    public static SyntaxException expectedToken(String expected, Token actual) {
        String message = String.format("期望 '%s'，但遇到了 '%s'", expected, actual.name);
        return new SyntaxException(message, actual.location);
    }

    /**
     * 意外的符号
     */
    public static SyntaxException unexpectedToken(Token token) {
        String message = String.format("意外的符号 '%s'", token.name);
        return new SyntaxException(message, token.location);
    }

    /**
     * 缺少必要的符号
     */
    public static SyntaxException missingToken(String missing, TokenLocation location) {
        String message = String.format("缺少 '%s'", missing);
        return new SyntaxException(message, location);
    }

    /**
     * 括号不匹配
     */
    public static SyntaxException unmatchedBracket(String bracket, TokenLocation location) {
        String message = String.format("括号 '%s' 不匹配", bracket);
        return new SyntaxException(message, location);
    }

    /**
     * 非法的标识符
     */
    public static SyntaxException illegalIdentifier(String identifier, TokenLocation location) {
        String message = String.format("非法的标识符 '%s'", identifier);
        return new SyntaxException(message, location);
    }
}
