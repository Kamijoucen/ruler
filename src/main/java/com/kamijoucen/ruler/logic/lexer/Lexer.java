package com.kamijoucen.ruler.logic.lexer;

import com.kamijoucen.ruler.types.common.Constant;
import com.kamijoucen.ruler.types.lexer.LexerMode;
import com.kamijoucen.ruler.types.lexer.LexerState;
import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenLookUp;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.IOUtil;

/** Stateless tokenization; each scan owns its LexerState. */
public final class Lexer {
    private Lexer() {}

    public static List<Token> scan(String content, String fileName) {
        LexerState scan = new LexerState(content, fileName);
        List<Token> tokens = new ArrayList<>();
        Token token;
        do {
            token = nextToken(scan);
            tokens.add(token);
        } while (token.type != TokenType.EOF);
        return tokens;
    }

    public static Token nextToken(LexerState scan) {

        // 本次扫描是否匹配到 Token
        boolean match = false;

        if (isOver(scan)) {
            makeEndToken(scan);
            return scan.currentToken;
        }

        while (isNotOver(scan) && !match) {

            char ch = charAt(scan);

            if (IOUtil.isWhitespace(ch)) {
                scan.mode = LexerMode.NONE;
            } else if (IOUtil.isFirstIdentifierChar(ch)) {
                scan.mode = LexerMode.IDENTIFIER;
            } else if (ch == '$') {
                scan.mode = LexerMode.OUT_IDENTIFIER;
            } else if (Character.isDigit(ch)) {
                scan.mode = LexerMode.NUMBER;
            } else if (ch == '"' || ch == '\'') {
                if (ch == '\"' && peekChar(scan) == '\"' && peekChar(scan, 2) == '\"') {
                    scan.mode = LexerMode.STRING_BLOCK;
                } else {
                    scan.curStringFlag = ch;
                    scan.mode = LexerMode.STRING;
                }
            } else if (ch == '/' && peekChar(scan) == '/') {
                scan.mode = LexerMode.COMMENT;
            } else if (ch == '`') {
                scan.mode = LexerMode.STRING_IDENTIFIER;
            } else {
                scan.mode = LexerMode.SYMBOL;
            }

            if (scan.mode != LexerMode.NONE && scan.mode != LexerMode.COMMENT) {
                match = true;
                scan.tokenStartLine = scan.line;
                scan.tokenStartColumn = scan.column;
            }

            switch (scan.mode) {
                case NONE:
                    skipSpace(scan);
                    break;
                case IDENTIFIER:
                    scanIdentifier(scan);
                    break;
                case OUT_IDENTIFIER:
                    scanOutIdentifier(scan);
                    break;
                case NUMBER:
                    scanNumber(scan);
                    break;
                case STRING:
                    scanString(scan);
                    break;
                case STRING_BLOCK:
                    scanStringBlock(scan);
                    break;
                case SYMBOL:
                    scanSymbol(scan);
                    break;
                case COMMENT:
                    scanComment(scan);
                    break;
                case STRING_IDENTIFIER:
                    scanStringIdentifier(scan);
                    break;
            }
        }
        if (!match) {
            makeEndToken(scan);
        }
        return scan.currentToken;
    }

    private static void scanStringIdentifier(LexerState scan) {

        forward(scan);

        while (isNotOver(scan) && charAt(scan) != '`') {
            appendAndForward(scan);
        }

        forward(scan);

        makeToken(scan, TokenType.IDENTIFIER);
    }

    private static void scanComment(LexerState scan) {

        forward(scan);

        do {
            forward(scan);
        } while (isNotOver(scan) && charAt(scan) != '\n');

    }

    private static void scanSymbol(LexerState scan) {

        int step = 0;
        String symbol = charAt(scan) + "";

        forward(scan);

        symbol += peekChar(scan, step++);
        symbol += peekChar(scan, step++);

        TokenType type = TokenLookUp.symbol(symbol);
        while (step > 0 && type == TokenType.UN_KNOW) {
            symbol = symbol.substring(0, step--);
            type = TokenLookUp.symbol(symbol);
        }
        if (type == TokenType.UN_KNOW) {
            throw new SyntaxException("unknown symbol '" + symbol + "'",
                    new TokenLocation(scan.line, scan.column, scan.fileName));
        }
        forward(scan, step);
        append(scan, symbol);
        makeToken(scan, type);
    }

    private static void scanStringBlock(LexerState scan) {
        forward(scan, 3);
        while (isNotOver(scan) && !(charAt(scan) == '\"' && peekChar(scan) == '\"' && peekChar(scan, 2) == '\"')) {
            appendAndForward(scan);
        }
        if (isOver(scan)) {
            throw new SyntaxException("unterminated string literal",
                    new TokenLocation(scan.line, scan.column, scan.fileName));
        }
        forward(scan, 3);
        makeToken(scan, TokenType.STRING, '"');
    }

    private static void scanString(LexerState scan) {
        forward(scan);
        while (isNotOver(scan) && charAt(scan) != scan.curStringFlag) {
            if (charAt(scan) == '\\') {
                appendAndForward(scan); // append backslash
                if (isNotOver(scan)) {
                    appendAndForward(scan); // append escaped char
                }
                continue;
            }
            if (isOver(scan)) {
                throw new SyntaxException("unterminated string literal",
                        new TokenLocation(scan.line, scan.column, scan.fileName));
            }
            appendAndForward(scan);
        }
        if (isOver(scan)) {
            throw new SyntaxException("unterminated string literal",
                    new TokenLocation(scan.line, scan.column, scan.fileName));
        }
        forward(scan);
        makeToken(scan, TokenType.STRING, scan.curStringFlag);
    }

    private static void scanNumber(LexerState scan) {

        do {
            appendAndForward(scan);
        } while (isNotOver(scan) && Character.isDigit(charAt(scan)));

        if (isOver(scan) || charAt(scan) != '.') {
            makeToken(scan, TokenType.INTEGER);
            return;
        }
        if (Character.isDigit(peekChar(scan))) {
            appendAndForward(scan);
            int len = 0;
            while (isNotOver(scan) && Character.isDigit(charAt(scan))) {
                appendAndForward(scan);
                len++;
            }
            if (len == 0) {
                throw new SyntaxException("invalid number format '" + scan.buffer + "'",
                        new TokenLocation(scan.line, scan.column, scan.fileName));
            }
            makeToken(scan, TokenType.DOUBLE);
        } else {
            makeToken(scan, TokenType.INTEGER);
        }
    }

    private static void scanOutIdentifier(LexerState scan) {
        forward(scan);

        if (safeCharAt(scan) == '`') {
            forward(scan);

            while (isNotOver(scan) && charAt(scan) != '`') {
                appendAndForward(scan);
            }

            forward(scan);
        } else {
            if (isOver(scan) || !IOUtil.isFirstIdentifierChar(charAt(scan))) {
                throw new SyntaxException("illegal identifier '" + charAt(scan) + "'",
                        new TokenLocation(scan.line, scan.column, scan.fileName));
            }
            int len = 0;
            while (isNotOver(scan) && IOUtil.isIdentifierChar(charAt(scan))) {
                appendAndForward(scan);
                len++;
            }
            if (len == 0) {
                throw new SyntaxException("illegal identifier '" + scan.buffer + "'",
                        new TokenLocation(scan.line, scan.column, scan.fileName));
            }
        }
        makeToken(scan, TokenType.OUT_IDENTIFIER);
    }

    private static void scanIdentifier(LexerState scan) {
        do {
            appendAndForward(scan);
        } while (isNotOver(scan) && IOUtil.isIdentifierChar(charAt(scan)));
        TokenType tokenType = TokenLookUp.keyWords(scan.buffer.toString());
        if (tokenType != TokenType.UN_KNOW) {
            makeToken(scan, tokenType);
        } else {
            makeToken(scan, TokenType.IDENTIFIER);
        }
    }

    private static void skipSpace(LexerState scan) {
        while (isNotOver(scan) && IOUtil.isWhitespace(charAt(scan))) {
            forward(scan);
        }
    }

    private static void makeToken(LexerState scan, TokenType type) {
        scan.currentToken = new Token(type, scan.buffer.toString(),
                new TokenLocation(scan.line, scan.column, scan.fileName), '\0', scan.tokenStartLine, scan.tokenStartColumn);
        scan.buffer.delete(0, scan.buffer.length());
    }

    private static void makeToken(LexerState scan, TokenType type, char stringFlag) {
        scan.currentToken = new Token(type, scan.buffer.toString(),
                new TokenLocation(scan.line, scan.column, scan.fileName), stringFlag, scan.tokenStartLine, scan.tokenStartColumn);
        scan.buffer.delete(0, scan.buffer.length());
    }

    private static void makeEndToken(LexerState scan) {
        if (scan.isEnd) {
            return;
        }
        scan.currentToken = new Token(TokenType.EOF, "", new TokenLocation(scan.line, scan.column, scan.fileName));
        scan.buffer.delete(0, scan.buffer.length());
        scan.isEnd = true;
    }

    private static boolean isOver(LexerState scan) {
        return !isNotOver(scan);
    }

    private static boolean isNotOver(LexerState scan) {
        return scan.offset < scan.content.length();
    }


    private static void appendAndForward(LexerState scan) {
        append(scan);
        forward(scan);
    }


    private static void append(LexerState scan, String str) {
        scan.buffer.append(str);
    }

    private static void append(LexerState scan) {
        scan.buffer.append(charAt(scan));
    }

    private static void forward(LexerState scan, int step) {
        for (int i = 0; i < step; i++) {
            forward(scan);
        }
    }

    private static void forward(LexerState scan) {
        char current = charAt(scan);
        scan.offset = scan.offset + 1;
        if (current != '\n') {
            ++scan.column;
        } else {
            scan.column = 0;
            ++scan.line;
        }
    }

    private static char peekChar(LexerState scan) {
        return peekChar(scan, 1);
    }

    private static char peekChar(LexerState scan, int step) {
        if (scan.offset + step < scan.content.length()) {
            return charAt(scan, step);
        } else {
            return Constant.EOF;
        }
    }

    private static char safeCharAt(LexerState scan) {
        if (isNotOver(scan)) {
            return charAt(scan);
        } else {
            return Constant.EOF;
        }
    }

    private static char charAt(LexerState scan) {
        return scan.content.charAt(scan.offset);
    }

    private static char charAt(LexerState scan, int i) {
        return scan.content.charAt(scan.offset + i);
    }

}
