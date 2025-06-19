package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.config.MessageManager;
import com.kamijoucen.ruler.exception.*;
import com.kamijoucen.ruler.token.TokenLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息管理器实现
 * 负责构建格式化的错误消息，并创建对应的异常
 *
 * @author Kamijoucen
 */
public class MessageManagerImpl implements MessageManager {

    private final Map<MessageType, String> messageMap = new HashMap<>();

    public MessageManagerImpl() {
        initMessages();
    }

    private void initMessages() {
        // 词法错误消息
        messageMap.put(MessageType.UNKNOWN_SYMBOL, "未知符号 '%s'");
        messageMap.put(MessageType.NOT_FOUND_STRING_END, "字符串未正确结束");
        messageMap.put(MessageType.NUMBER_FORMAT_ERROR, "数字格式错误 '%s'");
        messageMap.put(MessageType.ILLEGAL_IDENTIFIER, "非法标识符 '%s'");

        // 语法错误消息
        messageMap.put(MessageType.BREAK_NOT_IN_LOOP, "break语句只能在循环内使用");
        messageMap.put(MessageType.CONTINUE_NOT_IN_LOOP, "continue语句只能在循环内使用");
        messageMap.put(MessageType.UNKNOWN_EXPRESSION_START, "无法识别的表达式开始 '%s'");

        // 运行时错误消息
        messageMap.put(MessageType.VARIABLE_NOT_DEFINED, "变量未定义 '%s'");
    }

    @Override
    public String buildMessage(MessageType type, TokenLocation location, String... args) {
        String message = messageMap.get(type);
        if (message == null) {
            message = "未知错误类型: " + type;
        }

        // 格式化消息
        String formattedMessage = formatMessage(message, args);

        // 添加位置信息
        if (location != null) {
            formattedMessage += " 位置: " + buildLocation(location);
        }

        return formattedMessage;
    }

    /**
     * 根据消息类型创建对应的异常
     *
     * @param type 消息类型
     * @param location 错误位置
     * @param args 消息参数
     * @return 对应的异常实例
     */
    public RulerException buildException(MessageType type, TokenLocation location, String... args) {
        switch (type) {
            case UNKNOWN_SYMBOL:
                return new LexicalException("未知符号 '" + args[0] + "'", location);

            case NOT_FOUND_STRING_END:
                return new LexicalException("字符串未正确结束", location);

            case NUMBER_FORMAT_ERROR:
                return new LexicalException("数字格式错误: " + args[0], location);

            case ILLEGAL_IDENTIFIER:
                return new LexicalException("非法标识符: " + args[0], location);

            case BREAK_NOT_IN_LOOP:
                return new SyntaxException("break语句只能在循环内使用", location);

            case CONTINUE_NOT_IN_LOOP:
                return new SyntaxException("continue语句只能在循环内使用", location);

            case UNKNOWN_EXPRESSION_START:
                return new SyntaxException("无法识别的表达式开始: " + args[0], location);

            case VARIABLE_NOT_DEFINED:
                return new VariableException("变量未定义: " + args[0], location);

            default:
                return new RulerRuntimeException("未知错误类型: " + type, location);
        }
    }

    /**
     * 格式化消息
     */
    private String formatMessage(String template, String... args) {
        try {
            return String.format(template, (Object[]) args);
        } catch (Exception e) {
            // 如果格式化失败，返回原始模板和参数
            return template + " (参数: " + String.join(", ", args) + ")";
        }
    }

    /**
     * 构建位置信息字符串
     */
    private String buildLocation(TokenLocation location) {
        if (location == null) {
            return "未知位置";
        }

        if (location.fileName != null && !location.fileName.isEmpty()) {
            return String.format("%s:%d:%d", location.fileName, location.line, location.column);
        }

        return String.format("%d:%d", location.line, location.column);
    }
}
