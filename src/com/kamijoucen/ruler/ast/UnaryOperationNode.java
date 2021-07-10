package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryOperationNode implements BaseNode {

    public final TokenType op;

    public final BaseNode exp;

    public UnaryOperationNode(TokenType op, BaseNode exp) {
        this.op = op;
        this.exp = exp;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }
}
