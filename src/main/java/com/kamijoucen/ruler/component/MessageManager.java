package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.common.MessageType;
import com.kamijoucen.ruler.domain.token.TokenLocation;

public interface MessageManager {

    String buildMessage(MessageType type, TokenLocation location, String... args);

}
