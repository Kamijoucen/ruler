package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.expression.InfixDefinitionNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.IOUtil;

/**
 * 中缀定义解析器
 */
public class InfixParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_INFIX;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {

        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.KEY_INFIX);
        Token infixToken = tokenStream.token();
        // eat the infix token
        tokenStream.nextToken();
        // infix operation
        ClosureDefineNode functionNode = (ClosureDefineNode) new FunParser().parse(manager);
        String infixName = functionNode.getName();
        if (IOUtil.isBlank(infixName)) {
            throw SyntaxException.withSyntax("infix function name is blank!", infixToken);
        }
        return new InfixDefinitionNode(functionNode, infixToken.location);
    }
}