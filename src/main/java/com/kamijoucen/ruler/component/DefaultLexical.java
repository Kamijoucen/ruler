package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.common.Constant;
import com.kamijoucen.ruler.domain.common.State;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.token.TokenLookUp;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.IOUtil;

public class DefaultLexical implements Lexical {

    private final RulerConfiguration configuration;

    private int offset;
    public int line;
    public int column;
    private State state;
    private final String content;
    private Token currentToken;
    private final StringBuilder buffer;
    private boolean isEnd;
    private char curStringFlag;
    private final String fileName;

    public DefaultLexical(String content, String fileName, RulerConfiguration configuration) {
        this.offset = 0;
        this.content = content;
        this.state = State.NONE;
        this.fileName = fileName;
        this.isEnd = false;
        this.buffer = new StringBuilder();
        this.configuration = configuration;
    }

    @Override
    public Token getToken() {
        return currentToken;
    }

    @Override
    public Token nextToken() {

        // 本次扫描是否匹配到 Token
        boolean match = false;

        if (isOver()) {
            makeEndToken();
            return currentToken;
        }

        while (isNotOver() && !match) {

            char ch = charAt();

            if (IOUtil.isWhitespace(ch)) {
                state = State.NONE;
            } else if (IOUtil.isFirstIdentifierChar(ch)) {
                state = State.IDENTIFIER;
            } else if (ch == '$') {
                state = State.OUT_IDENTIFIER;
            } else if (Character.isDigit(ch)) {
                state = State.NUMBER;
            } else if (ch == '"' || ch == '\'') {
                if (ch == '\"' && peekChar() == '\"' && peekChar(2) == '\"') {
                    state = State.STRING_BLOCK;
                } else {
                    curStringFlag = ch;
                    state = State.STRING;
                }
            } else if (ch == '/' && peekChar() == '/') {
                state = State.COMMENT;
            } else if (ch == '`') {
                state = State.STRING_IDENTIFIER;
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
                case STRING_BLOCK:
                    scanStringBlock();
                    break;
                case SYMBOL:
                    scanSymbol();
                    break;
                case COMMENT:
                    scanComment();
                    break;
                case STRING_IDENTIFIER:
                    scanStringIdentifier();
                    break;
            }
        }
        if (!match) {
            makeEndToken();
        }
        return currentToken;
    }

    private void scanStringIdentifier() {

        forward();

        while (isNotOver() && charAt() != '`') {
            appendAndForward();
        }

        forward();

        makeToken(TokenType.IDENTIFIER);
    }

    @Override
    public void scanComment() {

        forward();

        do {
            forward();
        } while (isNotOver() && charAt() != '\n');

    }

    @Override
    public void scanSymbol() {

        int step = 0;
        String symbol = charAt() + "";

        forward();

        symbol += peekChar(step++);
        symbol += peekChar(step++);

        TokenType type = TokenLookUp.symbol(symbol);
        while (step > 0 && type == TokenType.UN_KNOW) {
            symbol = symbol.substring(0, step--);
            type = TokenLookUp.symbol(symbol);
        }
        if (type == TokenType.UN_KNOW) {
            throw new SyntaxException("unknown symbol '" + symbol + "'",
                    new TokenLocation(line, column, fileName));
        }
        forward(step);
        append(symbol);
        makeToken(type);
    }

    @Override
    public void scanStringBlock() {
        forward(3);
        while (isNotOver() && !(charAt() == '\"' && peekChar() == '\"' && peekChar(2) == '\"')) {
            appendAndForward();
        }
        if (isOver()) {
            throw new SyntaxException("unterminated string literal",
                    new TokenLocation(line, column, fileName));
        }
        forward(3);
        makeToken(TokenType.STRING);
    }

    @Override
    public void scanString() {
        forward();
        while (isNotOver() && charAt() != curStringFlag) {
            if (charAt() == '\\') {
                forward();
                appendAndForward();
            }
            if (isOver()) {
                throw new SyntaxException("unterminated string literal",
                        new TokenLocation(line, column, fileName));
            }
            appendAndForward();
        }
        if (isOver()) {
            throw new SyntaxException("unterminated string literal",
                    new TokenLocation(line, column, fileName));
        }
        forward();
        makeToken(TokenType.STRING);
    }

    @Override
    public void scanNumber() {

        do {
            appendAndForward();
        } while (isNotOver() && Character.isDigit(charAt()));

        if (isOver() || charAt() != '.') {
            makeToken(TokenType.INTEGER);
            return;
        }
        if (Character.isDigit(peekChar())) {
            appendAndForward();
            int len = 0;
            while (isNotOver() && Character.isDigit(charAt())) {
                appendAndForward();
                len++;
            }
            if (len == 0) {
                throw new SyntaxException("invalid number format '" + buffer + "'",
                        new TokenLocation(line, column, fileName));
            }
            makeToken(TokenType.DOUBLE);
        } else {
            makeToken(TokenType.INTEGER);
        }
    }

    @Override
    public void scanOutIdentifier() {
        forward();

        if (safeCharAt() == '`') {
            forward();

            while (isNotOver() && charAt() != '`') {
                appendAndForward();
            }

            forward();
        } else {
            if (isOver() || !IOUtil.isFirstIdentifierChar(charAt())) {
                throw new SyntaxException("illegal identifier '" + charAt() + "'",
                        new TokenLocation(line, column, fileName));
            }
            int len = 0;
            while (isNotOver() && IOUtil.isIdentifierChar(charAt())) {
                appendAndForward();
                len++;
            }
            if (len == 0) {
                throw new SyntaxException("illegal identifier '" + buffer + "'",
                        new TokenLocation(line, column, fileName));
            }
        }
        makeToken(TokenType.OUT_IDENTIFIER);
    }

    @Override
    public void scanIdentifier() {
        do {
            appendAndForward();
        } while (isNotOver() && IOUtil.isIdentifierChar(charAt()));
        TokenType tokenType = TokenLookUp.keyWords(buffer.toString());
        if (tokenType != TokenType.UN_KNOW) {
            makeToken(tokenType);
        } else {
            makeToken(TokenType.IDENTIFIER);
        }
    }

    private void skipSpace() {
        while (isNotOver() && IOUtil.isWhitespace(charAt())) {
            forward();
        }
    }

    private void makeToken(TokenType type) {
        currentToken =
                new Token(type, buffer.toString(), new TokenLocation(line, column, fileName));
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

    private void forward(int step) {
        for (int i = 0; i < step; i++) {
            forward();
        }
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
        return peekChar(1);
    }

    private char peekChar(int step) {
        if (offset + step < content.length()) {
            return charAt(step);
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

    public static String of(String msg, int line, int column) {
        return msg + ", at {line:" + line + ", column:" + column + "}";
    }
}
