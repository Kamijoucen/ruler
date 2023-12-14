package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;

public interface MessageManager {

    String buildMessage(String msg, TokenLocation location, Scope currentScope);
    
    // 未知的符号
    String unknownSymbol(String symbol, TokenLocation location);
    
    // 未找到字符串的结束符
    String notFoundStringEnd(char flag, TokenLocation location);

    // 数字格式错误
    String numberFormatError(String number, TokenLocation location);

    // 非法的标识符
    String illegalIdentifier(String identifier, TokenLocation location);
    
    // bread 未在循环中使用
    String breakNotInLoop(TokenLocation location);

    // continue 未在循环中使用
    String continueNotInLoop(TokenLocation location);

    
}
