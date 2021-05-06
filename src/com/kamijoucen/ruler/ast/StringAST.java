package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.Value;

public class StringAST implements BaseAST {

    public final Token token;

    public final String value;

    public StringAST(Token token) {
        this.token = token;
        this.value = token.name;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
