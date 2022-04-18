package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.operation.AssignOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class DotNode extends OperationNode {

    /**
     * only support call or identifier
     */
    private TokenType dotType;
    private String name;
    private List<BaseNode> param;
    private Operation operation;
    private AssignOperation assignOperation;

    public DotNode(TokenType dotType, String name, List<BaseNode> param, TokenLocation location) {
        super(location);
        this.dotType = dotType;
        this.name = name;
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
        return TokenType.DOT;
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

    public AssignOperation getAssignOperation() {
        return assignOperation;
    }

    public void setAssignOperation(AssignOperation assignOperation) {
        this.assignOperation = assignOperation;
    }
}
