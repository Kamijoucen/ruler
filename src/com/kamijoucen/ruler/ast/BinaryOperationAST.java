package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.Value;

public class BinaryOperationAST implements BaseAST {

    public final BaseAST exp1;

    public final BaseAST exp2;

    public final TokenType op;

    public BinaryOperationAST(TokenType op, BaseAST exp1, BaseAST exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
