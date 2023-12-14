package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.common.State;
import com.kamijoucen.ruler.compiler.Lexical;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenLookUp;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.IOUtil;

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
    private String fileName;

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
                curStringFlag = ch;
                state = State.STRING;
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
        forward();

        while (isNotOver() && charAt() != '\n') {
            forward();
        }

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
            String message = this.configuration.getMessageManager().unknownSymbol(symbol,
                    new TokenLocation(line, column, fileName));
            throw new SyntaxException(message);
        }
        forward(step);
        append(symbol);
        makeToken(type);
    }

    @Override
    public void scanString() {
        forward();
        while (isNotOver() && charAt() != curStringFlag) {
            if (charAt() == '\\') {
                forward();
                appendAndForward();
            }
            appendAndForward();
        }
        if (isOver()) {
            String message = this.configuration.getMessageManager().notFoundStringEnd(curStringFlag,
                    new TokenLocation(line, column, fileName));
            throw new SyntaxException(message);
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
        if (Character.isDigit(peekChar())) {
            appendAndForward();
            int len = 0;
            while (isNotOver() && Character.isDigit(charAt())) {
                appendAndForward();
                len++;
            }
            if (len == 0) {
                String message = this.configuration.getMessageManager().numberFormatError(buffer.toString(),
                        new TokenLocation(line, column, fileName));
                throw new SyntaxException(message);
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
                String message = configuration.getMessageManager().illegalIdentifier(charAt() + "",
                        new TokenLocation(line, column, fileName));
                throw new SyntaxException(message);
            }
            int len = 0;
            while (isNotOver() && IOUtil.isIdentifierChar(charAt())) {
                appendAndForward();
                len++;
            }
            if (len == 0) {
                String message = configuration.getMessageManager().illegalIdentifier(buffer.toString(),
                        new TokenLocation(line, column, fileName));
                throw new SyntaxException(message);
            }
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
        while (isNotOver() && IOUtil.isWhitespace(charAt())) {
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
        if (offset + 1 < content.length()) {
            return charAt(1);
        } else {
            return Constant.EOF;
        }
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
