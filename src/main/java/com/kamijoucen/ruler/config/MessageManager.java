package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.token.TokenLocation;

public interface MessageManager {

    String buildMessage(MessageType type, TokenLocation location, String... args);

}
