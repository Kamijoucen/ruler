package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.runtime.VisitorRepository;
import com.kamijoucen.ruler.value.BaseValue;

public class AssignAST implements BaseAST {

    private NameAST name;

    private BaseAST expression;

    public AssignAST(NameAST name, BaseAST expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public NameAST getName() {
        return name;
    }

    public void setName(NameAST name) {
        this.name = name;
    }

    public BaseAST getExpression() {
        return expression;
    }

    public void setExpression(BaseAST expression) {
        this.expression = expression;
    }
}
