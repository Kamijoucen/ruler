package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class BinaryOperationAST implements BaseAST {

    private BaseAST exp1;

    private BaseAST exp2;

    private TokenType op;

    public BinaryOperationAST(TokenType op, BaseAST exp1, BaseAST exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

    public BaseAST getExp1() {
        return exp1;
    }

    public void setExp1(BaseAST exp1) {
        this.exp1 = exp1;
    }

    public BaseAST getExp2() {
        return exp2;
    }

    public void setExp2(BaseAST exp2) {
        this.exp2 = exp2;
    }

    public TokenType getOp() {
        return op;
    }

    public void setOp(TokenType op) {
        this.op = op;
    }
}
