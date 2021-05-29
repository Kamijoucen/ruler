package com.kamijoucen.ruler.ast.op;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;


public class IndexNode implements OperationNode {

    private BaseNode param;

    public IndexNode(BaseNode param) {
        this.param = param;
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

    }

    @Override
    public void putOperationValue(BaseValue value) {

    }

    public BaseNode getParam() {
        return param;
    }

    public void setParam(BaseNode param) {
        this.param = param;
    }
}
