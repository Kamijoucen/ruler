package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.exception.NoImplException;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

public class Utils {

    public static NoImplException todo(String str) {
        return new NoImplException(str == null ? "还未实现" : str);
    }

    public static void assertToken(Token token, TokenType type) {
        if (token.type != type) {
            throw SyntaxException.withSyntax("预期符号为:" + type.toString() + ", 但是出现了'" + type.name() + "'", token);
        }
    }

}