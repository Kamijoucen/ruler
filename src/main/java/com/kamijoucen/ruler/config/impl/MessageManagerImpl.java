package com.kamijoucen.ruler.config.impl;

import java.util.HashMap;
import java.util.Map;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.config.MessageManager;
import com.kamijoucen.ruler.token.TokenLocation;

public class MessageManagerImpl implements MessageManager {

    private Map<MessageType, String> messageMap = new HashMap<>();

    public MessageManagerImpl() {
        messageMap.put(MessageType.UNKNOWN_SYMBOL, "Unknown symbol '%s' at %s");
        messageMap.put(MessageType.NOT_FOUND_STRING_END, "Not found string end at %s");
        messageMap.put(MessageType.NUMBER_FORMAT_ERROR, "Number format error '%s' at %s");
        messageMap.put(MessageType.ILLEGAL_IDENTIFIER, "Illegal identifier '%s' at %s");
        messageMap.put(MessageType.BREAK_NOT_IN_LOOP, "Break not in loop at %s");
        messageMap.put(MessageType.CONTINUE_NOT_IN_LOOP, "Continue not in loop at %s");
    }


    @Override
    public String buildMessage(MessageType type, TokenLocation location, String... args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buildMessage'");
    }


}
