package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;


public class IndexNode implements OperationNode {

    private Operation operation;

    private BaseValue operationValue;

    private BaseNode index;

    public IndexNode(BaseNode index) {
        this.index = index;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
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
    public void putOperationValue(BaseValue value) {
        this.operationValue = value;
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

    public BaseValue getOperationValue() {
        return operationValue;
    }

}
