package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.component.Parsers;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.domain.common.Constant;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.Objects;

/**
 * 变量定义解析器，解析 var 语句
 */
public class VarParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_VAR;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token varToken = tokenStream.token();

        // eat var
        AssertUtil.assertToken(varToken, TokenType.KEY_VAR);
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);

        // 解析变量名
        BaseNode nameNode = Parsers.IDENTIFIER_PARSER.parse(manager);
        Objects.requireNonNull(nameNode);
        String varName = ((NameNode) nameNode).name.name;
        if (Constant.isReservedName(varName)) {
            throw new SyntaxException("reserved name cannot be used as identifier: " + varName);
        }

        // 检查是否有赋值
        if (tokenStream.token().type == TokenType.ASSIGN) {
            tokenStream.nextToken();
            BaseNode expNode = manager.parseExpression();
            Objects.requireNonNull(expNode);
            return new VariableDefineNode(nameNode, expNode, varToken.location);
        } else {
            return new VariableDefineNode(nameNode, null, varToken.location);
        }
    }

}