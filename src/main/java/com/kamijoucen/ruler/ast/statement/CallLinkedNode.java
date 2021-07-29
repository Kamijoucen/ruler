package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallLinkedNode implements BaseNode {

    private BaseNode first;

    private List<OperationNode> calls;

    public CallLinkedNode(BaseNode first, List<OperationNode> calls) {
        this.first = first;
        this.calls = calls;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
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
