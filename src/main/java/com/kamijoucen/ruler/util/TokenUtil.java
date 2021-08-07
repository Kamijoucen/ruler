package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.token.TokenType;

public class TokenUtil {

    public static boolean isLogicOperation(TokenType type) {
        return type == TokenType.AND || type == TokenType.OR || type == TokenType.NOT;
    }

    public static String of(String msg, int line, int column) {
        return msg + ", 位置在{line:" + line + ", column:" + column + "}";
    }

}
