package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.component.DefaultLexical;
import com.kamijoucen.ruler.component.TokenStreamImpl;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.token.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenStreamImplTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private TokenStreamImpl createTokenStream(String content) {
        DefaultLexical lexical = new DefaultLexical(content, null, configuration);
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        return tokenStream;
    }

    @Test
    public void basicNextTokenTest() {
        TokenStreamImpl ts = createTokenStream("1 + 2");
        Assert.assertEquals(TokenType.INTEGER, ts.nextToken().type);
        Assert.assertEquals(TokenType.ADD, ts.nextToken().type);
        Assert.assertEquals(TokenType.INTEGER, ts.nextToken().type);
        Assert.assertEquals(TokenType.EOF, ts.nextToken().type);
    }

    @Test
    public void tokenCurrentTest() {
        TokenStreamImpl ts = createTokenStream("a b");
        ts.nextToken();
        Assert.assertEquals("a", ts.token().name);
        ts.nextToken();
        Assert.assertEquals("b", ts.token().name);
    }

    @Test
    public void peekTest() {
        TokenStreamImpl ts = createTokenStream("x y z");
        ts.nextToken();
        Assert.assertEquals("y", ts.peek().name);
        Assert.assertEquals("z", ts.peek(2).name);
    }

    @Test
    public void peekBeyondEndTest() {
        TokenStreamImpl ts = createTokenStream("x");
        ts.nextToken();
        Assert.assertEquals(TokenType.EOF, ts.peek(5).type);
    }

    @Test
    public void rollBackTokenTest() {
        TokenStreamImpl ts = createTokenStream("a b c");
        ts.nextToken();
        ts.nextToken();
        ts.rollBackToken();
        Assert.assertEquals("a", ts.token().name);
    }

    @Test
    public void rollBackTokenMultiStepTest() {
        TokenStreamImpl ts = createTokenStream("a b c");
        ts.nextToken();
        ts.nextToken();
        ts.nextToken();
        ts.rollBackToken(2);
        Assert.assertEquals("a", ts.token().name);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void nextTokenBeyondEndTest() {
        TokenStreamImpl ts = createTokenStream("a");
        ts.nextToken();
        ts.nextToken();
        ts.nextToken();
    }
}
