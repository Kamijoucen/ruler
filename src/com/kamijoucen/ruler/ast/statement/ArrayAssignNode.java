package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ArrayAssignNode implements BaseNode {

    private CallLinkedNode calls;

    private IndexNode index;

    private BaseNode expression;

    public ArrayAssignNode(CallLinkedNode calls, IndexNode index, BaseNode expression) {
        this.calls = calls;
        this.index = index;
        this.expression = expression;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public CallLinkedNode getCalls() {
        return calls;
    }

    public void setCalls(CallLinkedNode calls) {
        this.calls = calls;
    }

    public IndexNode getIndex() {
        return index;
    }

    public void setIndex(IndexNode index) {
        this.index = index;
    }

    public BaseNode getExpression() {
        return expression;
    }

    public void setExpression(BaseNode expression) {
        this.expression = expression;
    }
}
