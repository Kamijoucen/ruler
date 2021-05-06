package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.Value;

public class NumberAST implements BaseAST {

    public final Token value;

    public NumberAST(Token value) {
        this.value = value;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
