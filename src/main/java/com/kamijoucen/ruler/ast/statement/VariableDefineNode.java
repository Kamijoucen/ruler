package com.kamijoucen.ruler.ast.statement;


import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class VariableDefineNode extends AbstractBaseNode {

    private BaseNode name;

    private BaseNode expression;

    public VariableDefineNode(BaseNode name, BaseNode expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope, context);
    }

    public BaseNode getName() {
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
