package com.kamijoucen.ruler.lexical;

import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.state.State;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.util.MsgUtil;

public class DefaultLexical implements Lexical {

    private int offset;

    public int line;

    public int column;

    private State state;

    private String content;

    private Token currentToken;

    private StringBuilder buffer;


    private char curStringFlag;

    public DefaultLexical(String content) {
        this.offset = 0;
        this.content = content;
        this.state = State.NONE;
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

            if (Character.isSpaceChar(ch)) {
                state = State.NONE;
            } else if (IOUtil.isFirstIdentifierChar(ch)) {
                state = State.IDENTIFIER;
            } else if (ch == '_') {
                state = State.FUN_IDENTIFIER;
            } else if (Character.isDigit(ch)) {
                state = State.NUMBER;
            } else if (ch == '"' || ch == '\'') {
                curStringFlag = ch;
                state = State.STRING;
            } else {
                state = State.SYMBOL;
            }

            if (state != State.NONE) {
                match = true;
            }

            switch (state) {
                case NONE:
                    skipSpace();
                    break;
                case IDENTIFIER:
                    scanIdentifier();
                    break;
                case FUN_IDENTIFIER:
                    scanFunIdentifier();
                    break;
                case NUMBER:
                    scanNumber();
                    break;
                case STRING:
                    break;
                case SYMBOL:
                    break;
                case COMMENT:
                    break;
            }
        }

        if (!match) {
            makeEndToken();
        }

        return currentToken;
    }

    private void scanNumber() {
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
                    MsgUtil.of("小数点后未跟其他数字", line, column));
        }

        makeToken(TokenType.DOUBLE);
    }

    private void scanFunIdentifier() {
        forward();

        if (isOver() || !IOUtil.isFirstIdentifierChar(charAt())) {
            throw SyntaxException.withLexical(
                    MsgUtil.of("'" + safeCharAt() + "' 不是合法的标识符起始", line, column));
        }

        append(Constant.FUN_START);

        int len = 0;
        while (isNotOver() && IOUtil.isIdentifierChar(charAt())) {
            appendAndForward();
            len++;
        }

        if (len == 0) {
            throw SyntaxException.withLexical(
                    MsgUtil.of("函数标识符起始 '_' 后必须跟其他标识符", line, column));
        }

        makeToken(TokenType.FUN_IDENTIFIER);
    }

    private void scanIdentifier() {
        appendAndForward();
        while (isNotOver() && IOUtil.isIdentifierChar(charAt())) {
            appendAndForward();
        }
        makeToken(TokenType.IDENTIFIER);
    }

    private void skipSpace() {
        while (isNotOver() && Character.isSpaceChar(charAt())) {
            forward();
        }
    }

    private void makeToken(TokenType type) {
        currentToken = new Token(type, buffer.toString(), new TokenLocation(line, column));
        buffer.delete(0, buffer.length());
    }

    private void makeEndToken() {
        currentToken = new Token(TokenType.EOF, "", new TokenLocation(line, column));
        buffer.delete(0, buffer.length());
    }

    private boolean isOver() {
        return !isNotOver();
    }

    private boolean isNotOver() {
        return offset < content.length();
    }

    private void appendAndForward() {
        append();
        forward();
    }

    private void append(char ch) {
        buffer.append(ch);
    }

    private void append() {
        buffer.append(charAt());
    }

    private void forward() {
        if (++offset != '\n') {
            ++column;
        } else {
            column = 0;
            ++line;
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
        return content.charAt(i);
    }
}
