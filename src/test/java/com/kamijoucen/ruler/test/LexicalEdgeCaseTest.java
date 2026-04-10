package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.compiler.impl.DefaultLexical;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.parameter.RulerResult;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LexicalEdgeCaseTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    // ---------- escape sequences ----------

    @Test
    public void testEscapedQuote() {
        RulerResult r = compileScript("return \"a\\\"b\";").run();
        Assert.assertEquals("a\"b", r.first().toString());
    }

    @Test
    public void testEscapedNewLineActualBehavior() {
        // The current lexer does not translate \n into a newline;
        // it consumes the backslash and next char literally, then also consumes the following char.
        RulerResult r = compileScript("return \"a\\nb\";").run();
        Assert.assertEquals("anb", r.first().toString());
    }

    @Test
    public void testEscapedBackslash() {
        RulerResult r = compileScript("return \"a\\\\b\";").run();
        Assert.assertEquals("a\\b", r.first().toString());
    }

    // ---------- line comments ----------

    @Test
    public void testLineComment() {
        String script = "var a = 1; // this is a comment\nreturn a;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(1L, r.first().toInteger());
    }

    @Test
    public void testLineCommentAtEndOfFile() {
        String script = "return 42; // eof comment";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(42L, r.first().toInteger());
    }

    // ---------- floating point literals ----------

    @Test
    public void testDoubleLiteral() {
        RulerResult r = compileScript("return 3.14;").run();
        Assert.assertEquals(3.14, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testDoubleLiteralTrailingZero() {
        RulerResult r = compileScript("return 2.0;").run();
        Assert.assertEquals(2.0, r.first().toDouble(), 0.0001);
    }

    // ---------- unary operators ----------

    @Test
    public void testUnaryMinusExpression() {
        RulerResult r = compileScript("var a = 5; return -a;").run();
        Assert.assertEquals(-5L, r.first().toInteger());
    }

    @Test
    public void testUnaryPlusExpression() {
        RulerResult r = compileScript("var a = -3; return +a;").run();
        Assert.assertEquals(-3L, r.first().toInteger());
    }

    // ---------- token types directly ----------

    @Test
    public void testTokenLessThanOrEqual() {
        DefaultLexical lexical = new DefaultLexical("a <= b", null, configuration);
        while (lexical.nextToken().type != TokenType.IDENTIFIER) {
        }
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.LE, token.type);
    }

    @Test
    public void testTokenGreaterThanOrEqual() {
        DefaultLexical lexical = new DefaultLexical("a >= b", null, configuration);
        while (lexical.nextToken().type != TokenType.IDENTIFIER) {
        }
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.GE, token.type);
    }

    @Test
    public void testTokenArrow() {
        DefaultLexical lexical = new DefaultLexical("a -> b", null, configuration);
        while (lexical.nextToken().type != TokenType.IDENTIFIER) {
        }
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.ARROW, token.type);
    }

    @Test
    public void testTokenAssign() {
        DefaultLexical lexical = new DefaultLexical("a = b", null, configuration);
        while (lexical.nextToken().type != TokenType.IDENTIFIER) {
        }
        Token token = lexical.nextToken();
        Assert.assertEquals(TokenType.ASSIGN, token.type);
    }
}
