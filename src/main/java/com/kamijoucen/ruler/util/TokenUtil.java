package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.ast.facotr.OutNameNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

public class TokenUtil {

    public static BaseNode buildNameNode(Token token) {
        if (token.type == TokenType.IDENTIFIER) {
            return new NameNode(token);
        } else if (token.type == TokenType.OUT_IDENTIFIER) {
            return new OutNameNode(token);
        } else {
            throw SyntaxException.withSyntax("error identifier: " + token);
        }
    }

    public static boolean isLogicOperation(TokenType type) {
        return type == TokenType.AND || type == TokenType.OR || type == TokenType.NOT;
    }

    public static String of(String msg, int line, int column) {
        return msg + ", 位置在{line:" + line + ", column:" + column + "}";
    }

}
