package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.component.DefaultLexical;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultLexicalTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    @Test
    public void numberZeroTest() {
        DefaultLexical lexical = new DefaultLexical("0", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.INTEGER, token.type);
        Assert.assertEquals("0", token.name);
    }

    @Test
    public void numberIntegerTest() {
        DefaultLexical lexical = new DefaultLexical("123", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.INTEGER, token.type);
        Assert.assertEquals("123", token.name);
    }

    @Test
    public void numberDoubleTest() {
        DefaultLexical lexical = new DefaultLexical("123.456", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.DOUBLE, token.type);
        Assert.assertEquals("123.456", token.name);
    }

    @Test
    public void numberTrailingDotTest() {
        DefaultLexical lexical = new DefaultLexical("123.", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.INTEGER, token.type);
        Assert.assertEquals("123", token.name);
        Token dot = lexical.nextToken();
        Assert.assertEquals(TokenType.DOT, dot.type);
    }

    @Test
    public void commentSkipTest() {
        DefaultLexical lexical = new DefaultLexical("// this is a comment\nvar", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.KEY_VAR, token.type);
        Assert.assertEquals("var", token.name);
    }

    @Test
    public void commentAtEofTest() {
        DefaultLexical lexical = new DefaultLexical("// eof comment", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void eofStabilityTest() {
        DefaultLexical lexical = new DefaultLexical("", null, configuration);
        Assert.assertEquals(TokenType.EOF, lexical.nextToken().type);
        Assert.assertEquals(TokenType.EOF, lexical.nextToken().type);
        Assert.assertEquals(TokenType.EOF, lexical.nextToken().type);
    }

    @Test
    public void stringIdentifierTest() {
        DefaultLexical lexical = new DefaultLexical("`hello world`", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.IDENTIFIER, token.type);
        Assert.assertEquals("hello world", token.name);
    }

    @Test
    public void outIdentifierSimpleTest() {
        DefaultLexical lexical = new DefaultLexical("$score", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.OUT_IDENTIFIER, token.type);
        Assert.assertEquals("score", token.name);
    }

    @Test
    public void outIdentifierBacktickTest() {
        DefaultLexical lexical = new DefaultLexical("$`my var`", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.OUT_IDENTIFIER, token.type);
        Assert.assertEquals("my var", token.name);
    }

    @Test(expected = SyntaxException.class)
    public void outIdentifierIllegalTest() {
        DefaultLexical lexical = new DefaultLexical("$123", null, configuration);
        lexical.nextToken();
    }

    @Test
    public void stringBlockTest() {
        DefaultLexical lexical = new DefaultLexical("\"\"\"hello\\nworld\"\"\"", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.STRING, token.type);
        Assert.assertEquals("hello\\nworld", token.name);
    }

    @Test(expected = SyntaxException.class)
    public void unterminatedStringBlockTest() {
        DefaultLexical lexical = new DefaultLexical("\"\"\"hello", null, configuration);
        lexical.nextToken();
    }

    @Test(expected = SyntaxException.class)
    public void unterminatedStringTest() {
        DefaultLexical lexical = new DefaultLexical("\"hello", null, configuration);
        lexical.nextToken();
    }

    @Test
    public void keywordIfTest() {
        DefaultLexical lexical = new DefaultLexical("if", null, configuration);
        Assert.assertEquals(TokenType.KEY_IF, lexical.nextToken().type);
    }

    @Test
    public void keywordWhileTest() {
        DefaultLexical lexical = new DefaultLexical("while", null, configuration);
        Assert.assertEquals(TokenType.KEY_WHILE, lexical.nextToken().type);
    }

    @Test
    public void keywordFunTest() {
        DefaultLexical lexical = new DefaultLexical("fun", null, configuration);
        Assert.assertEquals(TokenType.KEY_FUN, lexical.nextToken().type);
    }

    @Test
    public void keywordVarTest() {
        DefaultLexical lexical = new DefaultLexical("var", null, configuration);
        Assert.assertEquals(TokenType.KEY_VAR, lexical.nextToken().type);
    }

    @Test
    public void keywordReturnTest() {
        DefaultLexical lexical = new DefaultLexical("return", null, configuration);
        Assert.assertEquals(TokenType.KEY_RETURN, lexical.nextToken().type);
    }

    @Test
    public void keywordTrueTest() {
        DefaultLexical lexical = new DefaultLexical("true", null, configuration);
        Assert.assertEquals(TokenType.KEY_TRUE, lexical.nextToken().type);
    }

    @Test
    public void keywordFalseTest() {
        DefaultLexical lexical = new DefaultLexical("false", null, configuration);
        Assert.assertEquals(TokenType.KEY_FALSE, lexical.nextToken().type);
    }

    @Test
    public void keywordNullTest() {
        DefaultLexical lexical = new DefaultLexical("null", null, configuration);
        Assert.assertEquals(TokenType.KEY_NULL, lexical.nextToken().type);
    }

    @Test
    public void identifierNotKeywordTest() {
        DefaultLexical lexical = new DefaultLexical("myVar", null, configuration);
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.IDENTIFIER, token.type);
        Assert.assertEquals("myVar", token.name);
    }

    @Test(expected = SyntaxException.class)
    public void unknownSymbolTest() {
        DefaultLexical lexical = new DefaultLexical("@", null, configuration);
        lexical.nextToken();
    }

    @Test
    public void dotBeforeNumberTest() {
        DefaultLexical lexical = new DefaultLexical(".123", null, configuration);
        Token dot = lexical.nextToken();
        Assert.assertEquals(TokenType.DOT, dot.type);
        Token num = lexical.nextToken();
        Assert.assertEquals(TokenType.INTEGER, num.type);
        Assert.assertEquals("123", num.name);
    }

}
