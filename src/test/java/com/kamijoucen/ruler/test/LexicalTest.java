package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Test;

import com.kamijoucen.ruler.logic.lexer.Lexer;
import com.kamijoucen.ruler.types.lexer.LexerState;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;

public class LexicalTest {

    // 三字符符号分词测试
    @Test
    public void strictNeTokenTest() {
        String s = "!==";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.STRICT_NE);

    }

    // 三字符符号全等于分词测试
    @Test
    public void strictEqTokenTest() {
        String s = "===";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.STRICT_EQ);
    }

    // 不等于分词测试
    @Test
    public void neTokenTest() {
        String s = "!=";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.NE);
    }

    // 等于分词测试
    @Test
    public void eqTokenTest() {
        String s = "==";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.EQ);
    }

    // 加号分词测试
    @Test
    public void plusTokenTest() {
        String s = "+";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.ADD);
    }

    // 减号分词测试
    @Test
    public void minusTokenTest() {
        String s = "-";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.SUB);
    }

    // 乘号分词测试
    @Test
    public void multiplyTokenTest() {
        String s = "*";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.MUL);
    }

    // 除号分词测试
    @Test
    public void divideTokenTest() {
        String s = "/";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.DIV);
    }

    // ++ 分词测试
    @Test
    public void incTokenTest() {
        String s = "++";
        LexerState lexical = new LexerState(s, null);
        Token nextToken = Lexer.nextToken(lexical);
        Assert.assertEquals(nextToken.type, TokenType.STRING_ADD);
    }

}
