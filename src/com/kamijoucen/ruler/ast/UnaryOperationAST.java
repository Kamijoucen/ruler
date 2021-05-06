package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.Value;

public class UnaryOperationAST implements BaseAST {

    public final TokenType op;

    public final BaseAST exp;

    public UnaryOperationAST(TokenType op, BaseAST exp) {
        this.op = op;
        this.exp = exp;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
