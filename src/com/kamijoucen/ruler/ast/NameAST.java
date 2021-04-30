package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.Value;

public class NameAST implements BaseAST {

    public final Token name;

    public final boolean isOut;

    public NameAST(Token name, boolean isOut) {
        this.name = name;
        this.isOut = isOut;
    }

    @Override

    public Value eval(Scope scope) {
        return null;
    }

}