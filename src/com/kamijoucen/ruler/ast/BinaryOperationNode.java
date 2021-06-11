package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class BinaryOperationNode implements BaseNode {

    private BaseNode exp1;

    private BaseNode exp2;

    private TokenType op;

    public BinaryOperationNode(TokenType op, BaseNode exp1, BaseNode exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

    public BaseNode getExp1() {
        return exp1;
    }

    public void setExp1(BaseNode exp1) {
        this.exp1 = exp1;
    }

    public BaseNode getExp2() {
        return exp2;
    }

    public void setExp2(BaseNode exp2) {
        this.exp2 = exp2;
    }

    public TokenType getOp() {
        return op;
    }

    public void setOp(TokenType op) {
        this.op = op;
    }
}