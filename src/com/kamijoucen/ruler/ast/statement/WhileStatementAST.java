package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class WhileStatementAST implements BaseAST {

    private BaseAST condition;

    private BaseAST block;

    public WhileStatementAST(BaseAST condition, BaseAST block) {
        this.condition = condition;
        this.block = block;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public BaseAST getCondition() {
        return condition;
    }

    public void setCondition(BaseAST condition) {
        this.condition = condition;
    }

    public BaseAST getBlock() {
        return block;
    }

    public void setBlock(BaseAST block) {
        this.block = block;
    }
}
