package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class AssignNode2 implements BaseNode {

    private BaseNode leftNode;

    private BaseNode expression;

    public AssignNode2(BaseNode leftNode, BaseNode expression) {
        this.leftNode = leftNode;
        this.expression = expression;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public BaseNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(BaseNode leftNode) {
        this.leftNode = leftNode;
    }

    public BaseNode getExpression() {
        return expression;
    }

    public void setExpression(BaseNode expression) {
        this.expression = expression;
    }
}
