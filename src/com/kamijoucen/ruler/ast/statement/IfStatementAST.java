package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class IfStatementAST implements BaseAST {

    private BaseAST condition;

    private BaseAST thenBlock;

    private BaseAST elseBlock;

    public IfStatementAST(BaseAST condition, BaseAST thenBlock, BaseAST elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    public BaseAST getCondition() {
        return condition;
    }

    public void setCondition(BaseAST condition) {
        this.condition = condition;
    }

    public BaseAST getThenBlock() {
        return thenBlock;
    }

    public void setThenBlock(BaseAST thenBlock) {
        this.thenBlock = thenBlock;
    }

    public BaseAST getElseBlock() {
        return elseBlock;
    }

    public void setElseBlock(BaseAST elseBlock) {
        this.elseBlock = elseBlock;
    }
}
