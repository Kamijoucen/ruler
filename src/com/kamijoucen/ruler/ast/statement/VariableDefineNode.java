package com.kamijoucen.ruler.ast.statement;


import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class VariableDefineNode implements BaseNode {

    private NameNode name;

    private BaseNode expression;

    public VariableDefineNode(NameNode name, BaseNode expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public NameNode getName() {
        return name;
    }

    public void setName(NameNode name) {
        this.name = name;
    }

    public BaseNode getExpression() {
        return expression;
    }

    public void setExpression(BaseNode expression) {
        this.expression = expression;
    }
}
