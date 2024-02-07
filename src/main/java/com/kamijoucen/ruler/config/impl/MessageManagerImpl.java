package com.kamijoucen.ruler.config.impl;

import java.util.HashMap;
import java.util.Map;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.config.MessageManager;
import com.kamijoucen.ruler.token.TokenLocation;

public class MessageManagerImpl implements MessageManager {

    private Map<MessageType, String> messageMap = new HashMap<>();

    public MessageManagerImpl() {
        messageMap.put(MessageType.UNKNOWN_SYMBOL, "Unknown symbol '%s'");
        messageMap.put(MessageType.NOT_FOUND_STRING_END, "Not found string end");
        messageMap.put(MessageType.NUMBER_FORMAT_ERROR, "Number format error '%s'");
        messageMap.put(MessageType.ILLEGAL_IDENTIFIER, "Illegal identifier '%s'");
        messageMap.put(MessageType.BREAK_NOT_IN_LOOP, "Break not in loop");
        messageMap.put(MessageType.CONTINUE_NOT_IN_LOOP, "Continue not in loop");
        messageMap.put(MessageType.VARIABLE_NOT_DEFINED, "Variable not defined '%s'");
    }


    @Override
    public String buildMessage(MessageType type, TokenLocation location, String... args) {
        String message = messageMap.get(type);
        if (message == null) {
            return "Unknown message type";
        }
        return String.format(message, (Object[]) args) + " at " + buildLocation(location);
    }

    private String buildLocation(TokenLocation location) {
        return location.line + ":" + location.column;
    }

}
