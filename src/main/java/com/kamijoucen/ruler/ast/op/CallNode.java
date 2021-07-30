package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallNode implements OperationNode {

    private Operation operation;

    private List<BaseNode> param;

    public CallNode(List<BaseNode> param) {
        this.param = param;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
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
    public void assign(BaseNode expression, Scope scope) {
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
