package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.token.TokenType;

public class TokenUtil {

    public static boolean isLogicOperation(TokenType type) {
        return type == TokenType.AND || type == TokenType.OR || type == TokenType.NOT;
    }

}
