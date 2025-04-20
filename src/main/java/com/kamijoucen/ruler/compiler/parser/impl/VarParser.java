package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

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
        BaseNode nameNode = parseIdentifier(manager);
        Objects.requireNonNull(nameNode);

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

    private BaseNode parseIdentifier(AtomParserManager manager) {
        // 委托给通用的标识符解析器
        // 这里可以考虑创建一个标识符解析器，或者将这个逻辑放在工具类中
        return manager.parseIdentifier();
    }
}