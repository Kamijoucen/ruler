package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class AssignNode implements BaseNode {

    private NameNode name;

    private BaseNode expression;

    public AssignNode(NameNode name, BaseNode expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public BaseValue eval(RuntimeContext context) {
        return VisitorRepository.getStatementVisitor().eval(this, context);
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
