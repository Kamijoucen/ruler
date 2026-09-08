package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.logic.lexer.Lexer;
import com.kamijoucen.ruler.types.parser.TokenStream;
import com.kamijoucen.ruler.types.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {

    private TokenStream createTokenStream(String content) {
        return new TokenStream(Lexer.scan(content, null));
    }

    @Test
    public void basicNextTokenTest() {
        TokenStream ts = createTokenStream("1 + 2");
        Assert.assertEquals(TokenType.INTEGER, ts.token().type);
        Assert.assertEquals(TokenType.ADD, ts.nextToken().type);
        Assert.assertEquals(TokenType.INTEGER, ts.nextToken().type);
        Assert.assertEquals(TokenType.EOF, ts.nextToken().type);
    }

    @Test
    public void tokenCurrentTest() {
        TokenStream ts = createTokenStream("a b");

        Assert.assertEquals("a", ts.token().name);
        ts.nextToken();
        Assert.assertEquals("b", ts.token().name);
    }

    @Test
    public void peekTest() {
        TokenStream ts = createTokenStream("x y z");

        Assert.assertEquals("y", ts.peek().name);
        Assert.assertEquals("z", ts.peek(2).name);
    }

    @Test
    public void peekBeyondEndTest() {
        TokenStream ts = createTokenStream("x");

        Assert.assertEquals(TokenType.EOF, ts.peek(5).type);
    }

    @Test
    public void rollBackTokenTest() {
        TokenStream ts = createTokenStream("a b c");

        ts.nextToken();
        ts.rollBackToken();
        Assert.assertEquals("a", ts.token().name);
    }

    @Test
    public void rollBackTokenMultiStepTest() {
        TokenStream ts = createTokenStream("a b c");

        ts.nextToken();
        ts.nextToken();
        ts.rollBackToken(2);
        Assert.assertEquals("a", ts.token().name);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void nextTokenBeyondEndTest() {
        TokenStream ts = createTokenStream("a");

        ts.nextToken();
        ts.nextToken();
    }
    @Test
    public void rollbackRestoresNewlineContext() {
        TokenStream tokens = createTokenStream("first\nsecond third");
        Assert.assertTrue(tokens.isNewLine());
        tokens.nextToken();
        Assert.assertTrue(tokens.isNewLine());
        tokens.nextToken();
        Assert.assertFalse(tokens.isNewLine());
        tokens.rollBackToken();
        Assert.assertEquals("second", tokens.token().name);
        Assert.assertTrue(tokens.isNewLine());
        tokens.rollBackToken();
        Assert.assertEquals("first", tokens.token().name);
        Assert.assertTrue(tokens.isNewLine());
    }
}
