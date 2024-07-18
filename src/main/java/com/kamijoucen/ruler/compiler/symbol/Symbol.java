package com.kamijoucen.ruler.compiler.symbol;

import com.kamijoucen.ruler.token.Token;

public class Symbol {

    public final Token token;

    public final boolean isConst;

    public final String type;

    // TODO 还需要表明该符号是定义还是引用

    public Symbol(Token token, boolean isConst, String type) {
        this.token = token;
        this.isConst = isConst;
        this.type = type;
    }

}
