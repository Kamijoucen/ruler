package com.kamijoucen.ruler.compiler.parser.impl;

import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.ReturnNode;
import com.kamijoucen.ruler.compiler.Parsers;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;

/**
 * 函数定义解析器
 */
public class FunParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_FUN;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token funToken = tokenStream.token();

        // eat fun
        AssertUtil.assertToken(funToken, TokenType.KEY_FUN);
        tokenStream.nextToken();

        // 解析静态捕获变量
        boolean isStaticCapture = false;
        List<BaseNode> capVarList = new ArrayList<>();

        if (tokenStream.token().type == TokenType.LEFT_SQUARE) {
            tokenStream.nextToken();
            isStaticCapture = true;
            if (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
                capVarList.add(Parsers.IDENTIFIER_PARSER.parse(manager));
            }
            while (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
                AssertUtil.assertToken(tokenStream, TokenType.COMMA);
                tokenStream.nextToken();
                capVarList.add(Parsers.IDENTIFIER_PARSER.parse(manager));
            }
            AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
            tokenStream.nextToken();
        }

        // 解析函数名（可选）
        String name = null;
        if (tokenStream.token().type == TokenType.IDENTIFIER) {
            name = tokenStream.token().name;
            tokenStream.nextToken();
        }

        // 解析参数列表
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        List<BaseNode> param = new ArrayList<>();
        if (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            BaseNode nameNode = Parsers.IDENTIFIER_PARSER.parse(manager);
            if (tokenStream.token().type == TokenType.ASSIGN) {
                tokenStream.nextToken();
                DefaultParamValNode paramValNode = new DefaultParamValNode((NameNode) nameNode,
                        manager.parseExpression(), nameNode.getLocation());
                param.add(paramValNode);
            } else {
                param.add(nameNode);
            }
        }

        while (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);

            BaseNode nameNode = Parsers.IDENTIFIER_PARSER.parse(manager);
            if (tokenStream.token().type == TokenType.ASSIGN) {
                tokenStream.nextToken();
                DefaultParamValNode paramValNode = new DefaultParamValNode((NameNode) nameNode,
                        manager.parseExpression(), nameNode.getLocation());
                param.add(paramValNode);
            } else {
                param.add(nameNode);
            }
        }

        // eat )
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();

        // 解析函数体
        BaseNode block;
        if (tokenStream.token().type == TokenType.ARROW) {
            // 箭头函数形式
            tokenStream.nextToken();
            BaseNode exp = manager.parseExpression();

            // 将表达式转换为return语句
            BaseNode returnNode = new ReturnNode(CollectionUtil.list(exp), exp.getLocation());
            BlockNode blockNode = new BlockNode(CollectionUtil.list(returnNode), exp.getLocation());
            block = blockNode;
        } else {
            // 代码块形式
            block = Parsers.BLOCK_PARSER.parse(manager);
        }

        return new ClosureDefineNode(name, param, block, isStaticCapture, capVarList, funToken.location);
    }
}