package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.ClosureDefineNode;
import com.kamijoucen.ruler.types.ast.DefaultParamValNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.ast.ReturnNode;

import com.kamijoucen.ruler.types.common.Constant;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.CollectionUtil;

/**
 * 函数定义解析器
 */
public class FunParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_FUN;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;
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
                capVarList.add(Parsers.IDENTIFIER_PARSER.parse(state));
            }
            while (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
                AssertUtil.assertToken(tokenStream, TokenType.COMMA);
                tokenStream.nextToken();
                capVarList.add(Parsers.IDENTIFIER_PARSER.parse(state));
            }
            AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
            tokenStream.nextToken();
        }

        // 解析函数名（可选）
        String name = null;
        if (tokenStream.token().type == TokenType.IDENTIFIER) {
            name = tokenStream.token().name;
            if (Constant.isReservedName(name)) {
                throw new SyntaxException("reserved name cannot be used as identifier: " + name);
            }
            tokenStream.nextToken();
        }

        // 解析参数列表
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        List<BaseNode> param = new ArrayList<>();
        if (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            BaseNode nameNode = Parsers.IDENTIFIER_PARSER.parse(state);
            String paramName = ((NameNode) nameNode).name.name;
            if (Constant.isReservedName(paramName)) {
                throw new SyntaxException("reserved name cannot be used as identifier: " + paramName);
            }
            if (tokenStream.token().type == TokenType.ASSIGN) {
                tokenStream.nextToken();
                DefaultParamValNode paramValNode = new DefaultParamValNode((NameNode) nameNode,
                        Parser.parseExpression(state), nameNode.getLocation());
                param.add(paramValNode);
            } else {
                param.add(nameNode);
            }
        }

        while (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);

            BaseNode nameNode = Parsers.IDENTIFIER_PARSER.parse(state);
            String paramName = ((NameNode) nameNode).name.name;
            if (Constant.isReservedName(paramName)) {
                throw new SyntaxException("reserved name cannot be used as identifier: " + paramName);
            }
            if (tokenStream.token().type == TokenType.ASSIGN) {
                tokenStream.nextToken();
                DefaultParamValNode paramValNode = new DefaultParamValNode((NameNode) nameNode,
                        Parser.parseExpression(state), nameNode.getLocation());
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
            BaseNode exp = Parser.parseExpression(state);

            // 将表达式转换为return语句
            BaseNode returnNode = new ReturnNode(CollectionUtil.list(exp), exp.getLocation());
            BlockNode blockNode = new BlockNode(CollectionUtil.list(returnNode), exp.getLocation());
            block = blockNode;
        } else {
            // 代码块形式
            block = Parsers.BLOCK_PARSER.parse(state);
        }

        return new ClosureDefineNode(name, param, block, isStaticCapture, capVarList, funToken.location);
    }
}
