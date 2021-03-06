package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.common.State;
import com.kamijoucen.ruler.compiler.Lexical;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenLookUp;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.util.TokenUtil;

public class DefaultLexical implements Lexical {

    private int offset;
    public int line;
    public int column;
    private State state;
    private String content;
    private Token currentToken;
    private StringBuilder buffer;
    private boolean isEnd;
    private char curStringFlag;
    String fileName;

    public DefaultLexical(String content, String fileName) {
        this.offset = 0;
        this.content = content;
        this.state = State.NONE;
        this.fileName = fileName;
        this.isEnd = false;
        this.buffer = new StringBuilder();
    }


    @Override
    public Token getToken() {
        return currentToken;
    }

    @Override
    public Token nextToken() {

        boolean match = false;

        if (isOver()) {
            makeEndToken();
            return currentToken;
        }

        while (isNotOver() && !match) {

            char ch = charAt();

            if (Character.isWhitespace(ch)) {
                state = State.NONE;
            } else if (IOUtil.isFirstIdentifierChar(ch)) {
                state = State.IDENTIFIER;
            } else if (ch == '$') {
                state = State.OUT_IDENTIFIER;
            } else if (Character.isDigit(ch)) {
                state = State.NUMBER;
            } else if (ch == '"' || ch == '\'') {
                curStringFlag = ch;
                state = State.STRING;
            } else if (ch == '/' && peekChar() == '/') {
                state = State.COMMENT;
            } else {
                state = State.SYMBOL;
            }

            if (state != State.NONE && state != State.COMMENT) {
                match = true;
            }

            switch (state) {
                case NONE:
                    skipSpace();
                    break;
                case IDENTIFIER:
                    scanIdentifier();
                    break;
                case OUT_IDENTIFIER:
                    scanOutIdentifier();
                    break;
                case NUMBER:
                    scanNumber();
                    break;
                case STRING:
                    scanString();
                    break;
                case SYMBOL:
                    scanSymbol();
                    break;
                case COMMENT:
                    scanComment();
                    break;
            }
        }
        if (!match) {
            makeEndToken();
        }
        return currentToken;
    }

    @Override
    public void scanComment() {

        forward();
        forward();

        while (isNotOver() && charAt() != '\n') {
            forward();
        }

    }

    @Override
    public void scanSymbol() {
        appendAndForward();
        append(safeCharAt());
        TokenType type = TokenLookUp.symbol(buffer.toString());
        if (type == TokenType.UN_KNOW) {
            buffer.delete(1, buffer.length());
            type = TokenLookUp.symbol(buffer.toString());
            if (type == TokenType.UN_KNOW) {
                throw SyntaxException.withLexical(
                        TokenUtil.of("???????????????:" + buffer.toString(), line, column));
            }
        } else {
            forward();
        }
        makeToken(type);
    }

    @Override
    public void scanString() {
        forward();
        while (isNotOver() && charAt() != curStringFlag) {
            appendAndForward();
        }
        if (isOver()) {
            throw SyntaxException.withLexical("???????????????????????????");
        }
        forward();
        makeToken(TokenType.STRING);
    }

    @Override
    public void scanNumber() {
        appendAndForward();
        while (isNotOver() && Character.isDigit(charAt())) {
            appendAndForward();
        }
        if (isOver() || charAt() != '.') {
            makeToken(TokenType.INTEGER);
            return;
        }
        appendAndForward();
        int len = 0;
        while (isNotOver() && Character.isDigit(charAt())) {
            appendAndForward();
            len++;
        }
        if (len == 0) {
            throw SyntaxException.withLexical(
                    TokenUtil.of("??????????????????????????????", line, column));
        }
        makeToken(TokenType.DOUBLE);
    }

    @Override
    public void scanOutIdentifier() {
        forward();
        if (isOver() || !IOUtil.isFirstIdentifierChar(charAt())) {
            throw SyntaxException.withLexical(
                    TokenUtil.of("'" + safeCharAt() + "' ??????????????????????????????", line, column));
        }
        int len = 0;
        while (isNotOver() && IOUtil.isIdentifierChar(charAt())) {
            appendAndForward();
            len++;
        }
        if (len == 0) {
            throw SyntaxException.withLexical(
                    TokenUtil.of("????????? '$' ???????????????????????????", line, column));
        }
        makeToken(TokenType.OUT_IDENTIFIER);
    }

    @Override
    public void scanIdentifier() {
        appendAndForward();
        while (isNotOver() && IOUtil.isIdentifierChar(charAt())) {
            appendAndForward();
        }
        TokenType tokenType = TokenLookUp.keyWords(buffer.toString());
        if (tokenType != TokenType.UN_KNOW) {
            makeToken(tokenType);
        } else {
            makeToken(TokenType.IDENTIFIER);
        }
    }

    private void skipSpace() {
        while (isNotOver() && Character.isWhitespace(charAt())) {
            forward();
        }
    }

    private void makeToken(TokenType type) {
        currentToken = new Token(type, buffer.toString(), new TokenLocation(line, column, fileName));
        buffer.delete(0, buffer.length());
    }

    private void makeEndToken() {
        if (isEnd) {
            return;
        }
        currentToken = new Token(TokenType.EOF, "", new TokenLocation(line, column, fileName));
        buffer.delete(0, buffer.length());
        isEnd = true;
    }

    private boolean isOver() {
        return !isNotOver();
    }

    private boolean isNotOver() {
        return offset < content.length();
    }

    private boolean isNotOver(int i) {
        return offset + i < content.length();
    }

    private void appendAndForward() {
        append();
        forward();
    }

    private void append(char ch) {
        buffer.append(ch);
    }

    private void append(String str) {
        buffer.append(str);
    }

    private void append() {
        buffer.append(charAt());
    }

    private void forward() {
        offset = offset + 1;
        if (safeCharAt() != '\n') {
            ++column;
        } else {
            column = 0;
            ++line;
        }
    }

    private char peekChar() {
        if (offset + 1 < content.length()) {
            return charAt(1);
        } else {
            return Constant.EOF;
        }
    }

    private char safeCharAt() {
        if (isNotOver()) {
            return charAt();
        } else {
            return Constant.EOF;
        }
    }

    private char charAt() {
        return content.charAt(offset);
    }

    private char charAt(int i) {
        return content.charAt(offset + i);
    }
}
