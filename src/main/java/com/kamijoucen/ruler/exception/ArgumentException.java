package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 参数异常
 * 表示函数调用时的参数错误
 *
 * @author Kamijoucen
 */
public class ArgumentException extends RulerException {

    private static final long serialVersionUID = 1L;

    public ArgumentException(String message, TokenLocation location) {
        super(ErrorType.ARGUMENT_ERROR, message, location);
    }

    public ArgumentException(String message, TokenLocation location, Throwable cause) {
        super(ErrorType.ARGUMENT_ERROR, message, location, cause);
    }

    /**
     * 参数数量不匹配
     */
    public static ArgumentException wrongArgumentCount(String functionName, int expected, int actual, TokenLocation location) {
        String message = String.format("函数 '%s' 期望 %d 个参数，实际传入 %d 个", functionName, expected, actual);
        return new ArgumentException(message, location);
    }

    /**
     * 参数类型错误
     */
    public static ArgumentException wrongArgumentType(String functionName, int position, String expected, String actual, TokenLocation location) {
        String message = String.format("函数 '%s' 的第 %d 个参数类型错误：期望 %s，实际为 %s",
                functionName, position, expected, actual);
        return new ArgumentException(message, location);
    }

    /**
     * 缺少必需的参数
     */
    public static ArgumentException missingRequiredArgument(String functionName, String paramName, TokenLocation location) {
        String message = String.format("函数 '%s' 缺少必需的参数 '%s'", functionName, paramName);
        return new ArgumentException(message, location);
    }

    /**
     * 参数值无效
     */
    public static ArgumentException invalidArgumentValue(String functionName, String paramName, String reason, TokenLocation location) {
        String message = String.format("函数 '%s' 的参数 '%s' 值无效：%s", functionName, paramName, reason);
        return new ArgumentException(message, location);
    }
}
