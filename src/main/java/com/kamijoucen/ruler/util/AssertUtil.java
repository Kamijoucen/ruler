package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.exception.NoImplException;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

public class AssertUtil {

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public static NoImplException todo(String str) {
        throw new NoImplException(str == null ? "还未实现" : str);
    }

    public static void assertToken(Token token, TokenType type) {
        if (token.type != type) {
            throw SyntaxException.withSyntax("预期符号为:" + type.toString() + ", 但是出现了'" + token.type.toString() + "'", token);
        }
    }

    public static void assertToken(TokenStream tokenStream, TokenType type) {
        if (tokenStream.token().type != type) {
            throw SyntaxException.withSyntax("预期符号为:" + type.toString() + ", 但是出现了'" + tokenStream.token().type.toString() + "'", tokenStream.token());
        }
    }
}
