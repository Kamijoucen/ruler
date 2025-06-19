package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.config.MessageManager;
import com.kamijoucen.ruler.exception.ErrorType;
import com.kamijoucen.ruler.exception.RulerException;
import com.kamijoucen.ruler.token.TokenLocation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 增强的消息管理器
 * 支持国际化、错误类型映射、消息格式化等功能
 *
 * @author Kamijoucen
 */
public class EnhancedMessageManager implements MessageManager {

    private final Map<MessageType, String> defaultMessages = new HashMap<>();
    private final Map<MessageType, ErrorType> errorTypeMapping = new HashMap<>();
    private ResourceBundle resourceBundle;
    private Locale locale = Locale.SIMPLIFIED_CHINESE;

    public EnhancedMessageManager() {
        initDefaultMessages();
        initErrorTypeMapping();
        loadResourceBundle();
    }

    private void initDefaultMessages() {
        // 词法错误
        defaultMessages.put(MessageType.UNKNOWN_SYMBOL, "未知符号 '%s'");
        defaultMessages.put(MessageType.NOT_FOUND_STRING_END, "字符串未正确结束");
        defaultMessages.put(MessageType.NUMBER_FORMAT_ERROR, "数字格式错误 '%s'");
        defaultMessages.put(MessageType.ILLEGAL_IDENTIFIER, "非法标识符 '%s'");

        // 语法错误
        defaultMessages.put(MessageType.BREAK_NOT_IN_LOOP, "break语句只能在循环内使用");
        defaultMessages.put(MessageType.CONTINUE_NOT_IN_LOOP, "continue语句只能在循环内使用");
        defaultMessages.put(MessageType.UNKNOWN_EXPRESSION_START, "无法识别的表达式开始 '%s'");

        // 运行时错误
        defaultMessages.put(MessageType.VARIABLE_NOT_DEFINED, "变量未定义 '%s'");
    }

    private void initErrorTypeMapping() {
        // 映射MessageType到ErrorType
        errorTypeMapping.put(MessageType.UNKNOWN_SYMBOL, ErrorType.LEXICAL_ERROR);
        errorTypeMapping.put(MessageType.NOT_FOUND_STRING_END, ErrorType.LEXICAL_ERROR);
        errorTypeMapping.put(MessageType.NUMBER_FORMAT_ERROR, ErrorType.LEXICAL_ERROR);
        errorTypeMapping.put(MessageType.ILLEGAL_IDENTIFIER, ErrorType.LEXICAL_ERROR);

        errorTypeMapping.put(MessageType.BREAK_NOT_IN_LOOP, ErrorType.SYNTAX_ERROR);
        errorTypeMapping.put(MessageType.CONTINUE_NOT_IN_LOOP, ErrorType.SYNTAX_ERROR);
        errorTypeMapping.put(MessageType.UNKNOWN_EXPRESSION_START, ErrorType.SYNTAX_ERROR);

        errorTypeMapping.put(MessageType.VARIABLE_NOT_DEFINED, ErrorType.RUNTIME_ERROR);
    }

    private void loadResourceBundle() {
        try {
            // 尝试加载资源文件，支持国际化
            resourceBundle = ResourceBundle.getBundle("ruler.messages", locale);
        } catch (Exception e) {
            // 如果没有资源文件，使用默认消息
            resourceBundle = null;
        }
    }

    @Override
    public String buildMessage(MessageType type, TokenLocation location, String... args) {
        String template = getMessage(type);
        String formattedMessage = formatMessage(template, args);

        if (location != null) {
            return formattedMessage + " 位置: " + formatLocation(location);
        }
        return formattedMessage;
    }

    /**
     * 构建带错误类型的消息
     */
    public String buildMessageWithType(MessageType type, TokenLocation location, String... args) {
        ErrorType errorType = getErrorType(type);
        String message = buildMessage(type, location, args);
        return String.format("[%s] %s", errorType.getDisplayName(), message);
    }

    /**
     * 获取消息模板
     */
    private String getMessage(MessageType type) {
        // 优先从资源文件获取
        if (resourceBundle != null) {
            try {
                return resourceBundle.getString(type.name());
            } catch (Exception ignored) {
            }
        }

        // 否则使用默认消息
        String message = defaultMessages.get(type);
        return message != null ? message : "未知错误类型: " + type;
    }

    /**
     * 格式化消息
     */
    private String formatMessage(String template, String... args) {
        try {
            return String.format(template, (Object[]) args);
        } catch (Exception e) {
            // 格式化失败时，返回原始模板
            return template + " (参数: " + String.join(", ", args) + ")";
        }
    }

    /**
     * 格式化位置信息
     */
    private String formatLocation(TokenLocation location) {
        if (location.fileName != null) {
            return String.format("%s:%d:%d", location.fileName, location.line, location.column);
        }
        return String.format("%d:%d", location.line, location.column);
    }

    /**
     * 获取错误类型
     */
    public ErrorType getErrorType(MessageType messageType) {
        return errorTypeMapping.getOrDefault(messageType, ErrorType.RUNTIME_ERROR);
    }

    /**
     * 设置语言环境
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        loadResourceBundle();
    }

    /**
     * 添加自定义消息
     */
    public void addMessage(MessageType type, String message) {
        defaultMessages.put(type, message);
    }

    /**
     * 添加错误类型映射
     */
    public void addErrorTypeMapping(MessageType messageType, ErrorType errorType) {
        errorTypeMapping.put(messageType, errorType);
    }

    @Override
    public RulerException buildException(MessageType type, TokenLocation location, String... args) {
        // 委托给MessageManagerImpl处理
        return new MessageManagerImpl().buildException(type, location, args);
    }
}
