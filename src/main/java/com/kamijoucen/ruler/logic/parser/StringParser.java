package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.StringInterpolationNode;
import com.kamijoucen.ruler.domain.ast.factor.StringNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.component.DefaultLexical;
import com.kamijoucen.ruler.component.TokenStreamImpl;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串字面量解析器（支持模板插值）
 */
public class StringParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.STRING;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token token = tokenStream.token();
        tokenStream.nextToken();

        char flag = token.stringFlag;
        // 单引号字符串不支持插值，直接返回原义文本
        if (flag == '\'') {
            return new StringNode(token.name, token.location);
        }

        List<BaseNode> parts = parseInterpolation(token.name, token.location, manager);
        if (parts == null || parts.isEmpty()) {
            return new StringNode(token.name, token.location);
        }
        if (parts.size() == 1 && parts.get(0) instanceof StringNode) {
            return parts.get(0);
        }
        return new StringInterpolationNode(parts, token.location);
    }

    private List<BaseNode> parseInterpolation(String text, TokenLocation location, AtomParserManager manager) {
        List<BaseNode> parts = new ArrayList<>();
        StringBuilder literal = new StringBuilder();
        int i = 0;
        int len = text.length();
        boolean hasInterpolation = false;

        while (i < len) {
            char ch = text.charAt(i);
            if (ch == '\\' && i + 1 < len) {
                char next = text.charAt(i + 1);
                if (next == '{' || next == '\\') {
                    // \{ 变成字面量 { ，\\ 变成字面量 \
                    literal.append(next);
                    i += 2;
                    continue;
                } else {
                    literal.append(ch);
                    literal.append(next);
                    i += 2;
                    continue;
                }
            }

            if (ch == '{' && i + 1 < len) {
                // 找到插值开始，且不是空括号
                int exprStart = i + 1;
                int exprEnd = findMatchingBrace(text, exprStart);
                if (exprEnd == -1) {
                    throw new SyntaxException("unterminated string interpolation '{'", location);
                }

                // 空插值 {} 抛异常
                if (exprEnd == exprStart) {
                    throw new SyntaxException("empty string interpolation is not allowed", location);
                }

                String exprText = text.substring(exprStart, exprEnd);
                exprText = unescapeExpr(exprText);
                hasInterpolation = true;

                // 将之前累积的文本片段写入 parts
                if (literal.length() > 0) {
                    parts.add(new StringNode(literal.toString(), location));
                    literal.setLength(0);
                }

                BaseNode exprNode = parseSubExpression(exprText, location, manager);
                parts.add(exprNode);

                i = exprEnd + 1;
                continue;
            }

            literal.append(ch);
            i++;
        }

        if (!hasInterpolation) {
            return null;
        }

        if (literal.length() > 0) {
            parts.add(new StringNode(literal.toString(), location));
        }
        return parts;
    }

    private int findMatchingBrace(String text, int start) {
        int depth = 0;
        boolean inString = false;
        char stringChar = '\0';
        int i = start;
        int len = text.length();
        while (i < len) {
            char ch = text.charAt(i);
            if (!inString && ch == '\\' && i + 1 < len) {
                char next = text.charAt(i + 1);
                if (next == '\"' || next == '\'') {
                    i += 2;
                    continue;
                }
            }
            if (inString) {
                if (ch == '\\' && i + 1 < len) {
                    i += 2;
                    continue;
                }
                if (ch == stringChar) {
                    inString = false;
                }
                i++;
                continue;
            }
            if (ch == '\"' || ch == '\'') {
                inString = true;
                stringChar = ch;
                i++;
                continue;
            }
            if (ch == '{') {
                depth++;
                i++;
                continue;
            }
            if (ch == '}') {
                if (depth == 0) {
                    return i;
                }
                depth--;
                i++;
                continue;
            }
            i++;
        }
        return -1;
    }

    private String unescapeExpr(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\\' && i + 1 < text.length()) {
                char next = text.charAt(i + 1);
                if (next == '\"' || next == '\'' || next == '\\' || next == '{' || next == '}') {
                    sb.append(next);
                    i++;
                    continue;
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    private BaseNode parseSubExpression(String exprText, TokenLocation location, AtomParserManager manager) {
        String fileName = location.fileName;
        DefaultLexical lexical = new DefaultLexical(exprText, fileName, manager.getConfiguration());
        TokenStreamImpl ts = new TokenStreamImpl(lexical);
        ts.scan();
        ts.nextToken();
        AtomParserManager subManager = new AtomParserManager(ts, manager.getConfiguration());
        try {
            BaseNode node = subManager.parseExpression();
            if (ts.token().type != TokenType.EOF) {
                throw new SyntaxException("illegal string interpolation expression '" + exprText + "'", location);
            }
            return node;
        } catch (NullPointerException e) {
            throw new SyntaxException("illegal string interpolation expression '" + exprText + "'", location);
        }
    }
}
