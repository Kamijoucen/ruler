package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kamijoucen.ruler.compiler.impl.DefaultLexical;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

public class LexicalTest {

    private RulerConfiguration configuration;
    
    // init config
    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    // 三字符符号分词测试
    @Test
    public void strictNeTokenTest() {
        String s = "!==";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.STRICT_NE);

    }

    // 三字符符号全等于分词测试
    @Test
    public void strictEqTokenTest() {
        String s = "===";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.STRICT_EQ);
    }

    // 不等于分词测试
    @Test
    public void neTokenTest() {
        String s = "!=";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.NE);
    }

    // 等于分词测试
    @Test
    public void eqTokenTest() {
        String s = "==";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.EQ);
    }

    // 加号分词测试
    @Test
    public void plusTokenTest() {
        String s = "+";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.ADD);
    }

    // 减号分词测试
    @Test
    public void minusTokenTest() {
        String s = "-";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.SUB);
    }

    // 乘号分词测试
    @Test
    public void multiplyTokenTest() {
        String s = "*";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.MUL);
    }

    // 除号分词测试
    @Test
    public void divideTokenTest() {
        String s = "/";
        DefaultLexical lexical = new DefaultLexical(s, null, configuration);
        Token nextToken = lexical.nextToken();
        Assert.assertEquals(nextToken.type, TokenType.DIV);
    }

}
