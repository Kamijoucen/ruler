package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.exception.NoImplException;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

/**
 * 断言工具类
 * 用于参数校验和语法检查
 *
 * @author Kamijoucen
 */
public class AssertUtil {

    /**
     * 私有构造函数，防止实例化
     */
    private AssertUtil() {
        throw new AssertionError("No instances of AssertUtil");
    }

    /**
     * 断言对象不为null
     *
     * @param obj 要检查的对象
     * @param message 对象为null时的错误信息
     * @throws IllegalArgumentException 如果对象为null
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象不为null
     *
     * @param obj 要检查的对象
     * @throws IllegalArgumentException 如果对象为null
     */
    public static void notNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
    }

    /**
     * 标记未实现的功能
     *
     * @param message 提示信息
     * @return 不会返回，总是抛出异常
     * @throws NoImplException 总是抛出
     */
    public static NoImplException TODO(String message) {
        throw new NoImplException(message != null ? message : "功能尚未实现");
    }

    /**
     * 断言token是指定类型
     *
     * @param token 要检查的token
     * @param type 期望的token类型
     * @throws SyntaxException 如果类型不匹配
     */
    public static void assertToken(Token token, TokenType type) {
        if (token.type != type) {
            throw SyntaxException.expectedToken(type.toString(), token);
        }
    }

    /**
     * 断言当前token是指定类型
     *
     * @param tokenStream token流
     * @param type 期望的token类型
     * @throws SyntaxException 如果类型不匹配
     */
    public static void assertToken(TokenStream tokenStream, TokenType type) {
        Token token = tokenStream.token();
        if (token.type != type) {
            throw SyntaxException.expectedToken(type.toString(), token);
        }
    }

    /**
     * 断言当前token是指定类型，并消费它
     *
     * @param tokenStream token流
     * @param type 期望的token类型
     * @throws SyntaxException 如果类型不匹配
     */
    public static void assertTokenAndSkip(TokenStream tokenStream, TokenType type) {
        assertToken(tokenStream, type);
        tokenStream.nextToken();
    }
}
