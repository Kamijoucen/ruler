package com.kamijoucen.ruler.ast.op;

import java.util.List;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class DotNode implements OperationNode {

    /**
     * only support call or identifier
     */
    private TokenType dotType;

    private String name;

    private List<BaseNode> param;

    private Operation operation;

    private BaseValue operationValue;

    public DotNode(TokenType dotType, String name, List<BaseNode> param) {
        this.dotType = dotType;
        this.name = name;
        this.param = param;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    @Override
    public TokenType getOperationType() {
        return TokenType.DOT;
    }

    @Override
    public void putOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void putOperationValue(BaseValue value) {
        this.operationValue = value;
    }

    public TokenType getDotType() {
        return dotType;
    }

    public void setDotType(TokenType dotType) {
        this.dotType = dotType;
    }

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public BaseValue getOperationValue() {
        return operationValue;
    }

    public void setOperationValue(BaseValue operationValue) {
        this.operationValue = operationValue;
    }
    
}
