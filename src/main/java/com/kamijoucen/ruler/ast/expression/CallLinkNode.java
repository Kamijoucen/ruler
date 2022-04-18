package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallLinkNode extends AbstractBaseNode {

    private BaseNode first;

    private List<OperationNode> calls;

    public CallLinkNode(BaseNode first, List<OperationNode> calls, TokenLocation location) {
        super(location);
        this.first = first;
        this.calls = calls;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        context.setCallLinkAssign(false);
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(RuntimeContext context, Scope scope) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public BaseValue evalAssign(RuntimeContext context, Scope scope) {
        context.setCallLinkAssign(true);
        return context.getNodeVisitor().eval(this, scope, context);
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
