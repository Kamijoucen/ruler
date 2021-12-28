package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallNode extends OperationNode {

    private Operation operation;

    private List<BaseNode> param;

    public CallNode(List<BaseNode> param) {
        this.param = param;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(RuntimeContext context, Scope scope) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    @Override
    public TokenType getOperationType() {
        return TokenType.CALL;
    }

    @Override
    public void putOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void putAssignOperation(AssignOperation operation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void assign(BaseNode expression, Scope scope, RuntimeContext context) {
        throw new UnsupportedOperationException();
    }

    public Operation getOperation() {
        return operation;
    }

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }
}
