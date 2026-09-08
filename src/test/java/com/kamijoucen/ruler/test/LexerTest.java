package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.logic.lexer.Lexer;
import com.kamijoucen.ruler.types.lexer.LexerState;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

    @Test
    public void numberZeroTest() {
        LexerState lexical = new LexerState("0", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.INTEGER, token.type);
        Assert.assertEquals("0", token.name);
    }

    @Test
    public void numberIntegerTest() {
        LexerState lexical = new LexerState("123", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.INTEGER, token.type);
        Assert.assertEquals("123", token.name);
    }

    @Test
    public void numberDoubleTest() {
        LexerState lexical = new LexerState("123.456", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.DOUBLE, token.type);
        Assert.assertEquals("123.456", token.name);
    }

    @Test
    public void numberTrailingDotTest() {
        LexerState lexical = new LexerState("123.", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.INTEGER, token.type);
        Assert.assertEquals("123", token.name);
        Token dot = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.DOT, dot.type);
    }

    @Test
    public void commentSkipTest() {
        LexerState lexical = new LexerState("// this is a comment\nvar", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.KEY_VAR, token.type);
        Assert.assertEquals("var", token.name);
    }

    @Test
    public void commentAtEofTest() {
        LexerState lexical = new LexerState("// eof comment", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void eofStabilityTest() {
        LexerState lexical = new LexerState("", null);
        Assert.assertEquals(TokenType.EOF, Lexer.nextToken(lexical).type);
        Assert.assertEquals(TokenType.EOF, Lexer.nextToken(lexical).type);
        Assert.assertEquals(TokenType.EOF, Lexer.nextToken(lexical).type);
    }

    @Test
    public void stringIdentifierTest() {
        LexerState lexical = new LexerState("`hello world`", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.IDENTIFIER, token.type);
        Assert.assertEquals("hello world", token.name);
    }

    @Test
    public void outIdentifierSimpleTest() {
        LexerState lexical = new LexerState("$score", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.OUT_IDENTIFIER, token.type);
        Assert.assertEquals("score", token.name);
    }

    @Test
    public void outIdentifierBacktickTest() {
        LexerState lexical = new LexerState("$`my var`", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.OUT_IDENTIFIER, token.type);
        Assert.assertEquals("my var", token.name);
    }

    @Test(expected = SyntaxException.class)
    public void outIdentifierIllegalTest() {
        LexerState lexical = new LexerState("$123", null);
        Lexer.nextToken(lexical);
    }

    @Test
    public void stringBlockTest() {
        LexerState lexical = new LexerState("\"\"\"hello\\nworld\"\"\"", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.STRING, token.type);
        Assert.assertEquals("hello\\nworld", token.name);
    }

    @Test(expected = SyntaxException.class)
    public void unterminatedStringBlockTest() {
        LexerState lexical = new LexerState("\"\"\"hello", null);
        Lexer.nextToken(lexical);
    }

    @Test(expected = SyntaxException.class)
    public void unterminatedStringTest() {
        LexerState lexical = new LexerState("\"hello", null);
        Lexer.nextToken(lexical);
    }

    @Test
    public void keywordIfTest() {
        LexerState lexical = new LexerState("if", null);
        Assert.assertEquals(TokenType.KEY_IF, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordWhileTest() {
        LexerState lexical = new LexerState("while", null);
        Assert.assertEquals(TokenType.KEY_WHILE, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordFunTest() {
        LexerState lexical = new LexerState("fun", null);
        Assert.assertEquals(TokenType.KEY_FUN, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordVarTest() {
        LexerState lexical = new LexerState("var", null);
        Assert.assertEquals(TokenType.KEY_VAR, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordReturnTest() {
        LexerState lexical = new LexerState("return", null);
        Assert.assertEquals(TokenType.KEY_RETURN, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordTrueTest() {
        LexerState lexical = new LexerState("true", null);
        Assert.assertEquals(TokenType.KEY_TRUE, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordFalseTest() {
        LexerState lexical = new LexerState("false", null);
        Assert.assertEquals(TokenType.KEY_FALSE, Lexer.nextToken(lexical).type);
    }

    @Test
    public void keywordNullTest() {
        LexerState lexical = new LexerState("null", null);
        Assert.assertEquals(TokenType.KEY_NULL, Lexer.nextToken(lexical).type);
    }

    @Test
    public void identifierNotKeywordTest() {
        LexerState lexical = new LexerState("myVar", null);
        Token token = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.IDENTIFIER, token.type);
        Assert.assertEquals("myVar", token.name);
    }

    @Test(expected = SyntaxException.class)
    public void unknownSymbolTest() {
        LexerState lexical = new LexerState("@", null);
        Lexer.nextToken(lexical);
    }

    @Test
    public void dotBeforeNumberTest() {
        LexerState lexical = new LexerState(".123", null);
        Token dot = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.DOT, dot.type);
        Token num = Lexer.nextToken(lexical);
        Assert.assertEquals(TokenType.INTEGER, num.type);
        Assert.assertEquals("123", num.name);
    }

    @Test
    public void independentScansKeepSeparateSourceLocations() {
        LexerState first = new LexerState("a\nb", "first");
        LexerState second = new LexerState("x", "second");
        Assert.assertEquals("a", Lexer.nextToken(first).name);
        Token other = Lexer.nextToken(second);
        Assert.assertEquals("x", other.name);
        Assert.assertEquals("second", other.location.fileName);
        Token next = Lexer.nextToken(first);
        Assert.assertEquals("b", next.name);
        Assert.assertEquals("first", next.location.fileName);
        Assert.assertEquals(1, next.startLine);
    }
}
