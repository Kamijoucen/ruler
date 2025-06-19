package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.exception.RulerException;
import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 消息管理器接口
 * 负责管理错误消息的格式化和异常创建
 *
 * @author Kamijoucen
 */
public interface MessageManager {

    /**
     * 构建格式化的错误消息
     *
     * @param type 消息类型
     * @param location 错误位置
     * @param args 消息参数
     * @return 格式化的错误消息
     */
    String buildMessage(MessageType type, TokenLocation location, String... args);

    /**
     * 根据消息类型创建对应的异常
     *
     * @param type 消息类型
     * @param location 错误位置
     * @param args 消息参数
     * @return 对应的异常实例
     */
    RulerException buildException(MessageType type, TokenLocation location, String... args);
}
