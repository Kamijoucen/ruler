package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallLinkNode implements BaseNode {

    private BaseNode first;

    private List<OperationNode> calls;

    public CallLinkNode(BaseNode first, List<OperationNode> calls) {
        this.first = first;
        this.calls = calls;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, false, scope, context);
    }

    public BaseValue evalAssign(RuntimeContext context, Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, true, scope, context);
    }

    public BaseNode getFirst() {
        return first;
    }

    public void setFirst(BaseNode first) {
        this.first = first;
    }

    public List<OperationNode> getCalls() {
        return calls;
    }

    public void setCalls(List<OperationNode> calls) {
        this.calls = calls;
    }
}
