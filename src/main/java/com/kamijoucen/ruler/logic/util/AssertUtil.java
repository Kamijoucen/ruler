package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.domain.exception.NoImplException;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

public class AssertUtil {

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new RulerRuntimeException(message);
        }
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new RulerRuntimeException("value is null");
        }
    }

    public static NoImplException TODO(String str) {
        throw new NoImplException(str == null ? "not implemented" : str);
    }

    public static void assertToken(Token token, TokenType type) {
        if (token.type != type) {
            throw new SyntaxException("expected token: " + type + ", but found: " + token.type + "\t token=" + token);
        }
    }

    public static void assertToken(TokenStream tokenStream, TokenType type) {
        if (tokenStream.token().type != type) {
            throw new SyntaxException("expected token: " + type + ", but found: " + tokenStream.token().type + "\t token=" + tokenStream.token());
        }
    }
}
