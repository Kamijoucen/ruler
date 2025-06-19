package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;

/**
 * Ruler解释器的基础异常类
 * 所有Ruler相关的异常都应该继承自此类
 *
 * @author Kamijoucen
 */
public abstract class RulerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误类型
     */
    private final ErrorType errorType;

    /**
     * 错误发生的位置
     */
    private final TokenLocation location;

    /**
     * 相关的源代码片段
     */
    private String sourceSnippet;

    /**
     * 构造函数
     *
     * @param errorType 错误类型
     * @param message 错误消息
     * @param location 错误位置
     */
    public RulerException(ErrorType errorType, String message, TokenLocation location) {
        super(message);
        this.errorType = errorType;
        this.location = location;
    }

    /**
     * 构造函数（带原因）
     *
     * @param errorType 错误类型
     * @param message 错误消息
     * @param location 错误位置
     * @param cause 原因
     */
    public RulerException(ErrorType errorType, String message, TokenLocation location, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.location = location;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public TokenLocation getLocation() {
        return location;
    }

    public String getSourceSnippet() {
        return sourceSnippet;
    }

    public void setSourceSnippet(String sourceSnippet) {
        this.sourceSnippet = sourceSnippet;
    }

    /**
     * 获取格式化的错误消息
     *
     * @return 格式化的错误消息
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();

        // 错误类型和消息
        sb.append("[").append(errorType.getDisplayName()).append("] ");
        sb.append(getMessage());

        // 位置信息
        if (location != null) {
            sb.append("\n位置: ");
            if (location.fileName != null && !location.fileName.isEmpty()) {
                sb.append(location.fileName).append(":");
            }
            sb.append(location.line).append(":").append(location.column);
        }

        // 源代码片段
        if (sourceSnippet != null && !sourceSnippet.isEmpty()) {
            sb.append("\n").append(sourceSnippet);
            if (location != null && location.column > 0) {
                sb.append("\n");
                for (int i = 1; i < location.column; i++) {
                    sb.append(" ");
                }
                sb.append("^");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }
}
