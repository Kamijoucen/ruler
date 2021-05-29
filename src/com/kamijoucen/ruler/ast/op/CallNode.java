package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallNode implements OperationNode {

    private Operation operation;

    private BaseValue operationValue;

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
    public void putOperationValue(BaseValue value) {
        this.operationValue = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public BaseValue getOperationValue() {
        return operationValue;
    }

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }
}
