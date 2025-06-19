package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;

/**
 * Ruler运行时异常
 * 表示脚本执行过程中的运行时错误
 *
 * @author Kamijoucen
 */
public class RulerRuntimeException extends RulerException {

    private static final long serialVersionUID = 1L;

    public RulerRuntimeException(String message, TokenLocation location) {
        super(ErrorType.RUNTIME_ERROR, message, location);
    }

    public RulerRuntimeException(String message, TokenLocation location, Throwable cause) {
        super(ErrorType.RUNTIME_ERROR, message, location, cause);
    }

    /**
     * 栈溢出
     */
    public static RulerRuntimeException stackOverflow(int maxDepth, TokenLocation location) {
        String message = String.format("调用栈深度超出限制，最大深度：%d", maxDepth);
        return new RulerRuntimeException(message, location);
    }

    /**
     * 循环次数超限
     */
    public static RulerRuntimeException loopLimitExceeded(int maxCount, TokenLocation location) {
        String message = String.format("循环次数超出限制，最大次数：%d", maxCount);
        return new RulerRuntimeException(message, location);
    }

    /**
     * 索引越界
     */
    public static RulerRuntimeException indexOutOfBounds(int index, int size, TokenLocation location) {
        String message = String.format("索引越界：索引=%d，大小=%d", index, size);
        return new RulerRuntimeException(message, location);
    }

    /**
     * 除零错误
     */
    public static RulerRuntimeException divisionByZero(TokenLocation location) {
        return new RulerRuntimeException("除零错误", location);
    }

    /**
     * 空指针访问
     */
    public static RulerRuntimeException nullAccess(String operation, TokenLocation location) {
        String message = String.format("对null值执行 '%s' 操作", operation);
        return new RulerRuntimeException(message, location);
    }

    /**
     * break不在循环中
     */
    public static RulerRuntimeException breakNotInLoop(TokenLocation location) {
        return new RulerRuntimeException("break语句必须在循环体内", location);
    }

    /**
     * continue不在循环中
     */
    public static RulerRuntimeException continueNotInLoop(TokenLocation location) {
        return new RulerRuntimeException("continue语句必须在循环体内", location);
    }

    /**
     * IO错误
     */
    public static RulerRuntimeException ioError(String operation, String path, TokenLocation location, Throwable cause) {
        String message = String.format("IO操作失败：%s '%s'", operation, path);
        return new RulerRuntimeException(message, location, cause);
    }
}
