package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;


public class IndexNode extends OperationNode {

    private Operation operation;

    private BaseNode index;

    private AssignOperation assignOperation;

    public IndexNode(BaseNode index) {
        this.index = index;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public TokenType getOperationType() {
        return TokenType.INDEX;
    }

    @Override
    public void putOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void putAssignOperation(AssignOperation operation) {
        this.assignOperation = operation;
    }

    @Override
    public void assign(BaseNode expression, Scope scope, RuntimeContext context) {
        this.assignOperation.assign(scope.getCallLinkPreviousValue(), this, expression, scope, context);
    }

    public BaseNode getIndex() {
        return index;
    }

    public void setIndex(BaseNode index) {
        this.index = index;
    }

    public Operation getOperation() {
        return operation;
    }

    public AssignOperation getAssignOperation() {
        return assignOperation;
    }

    public void setAssignOperation(AssignOperation assignOperation) {
        this.assignOperation = assignOperation;
    }
}
