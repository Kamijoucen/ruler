package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.domain.ast.expression.InfixDefinitionNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.common.Constant;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.IOUtil;

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
            throw new SyntaxException("infix function name is blank!\t token=" + infixToken);
        }
        if (Constant.isReservedName(infixName)) {
            throw new SyntaxException("reserved name cannot be used as identifier: " + infixName);
        }
        return new InfixDefinitionNode(functionNode, infixToken.location);
    }
}