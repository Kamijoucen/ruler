package com.kamijoucen.ruler.ast.op;

import java.util.List;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class DotNode implements OperationNode {

    /**
     * only support call or identifier
     */
    private TokenType dotType;

    private List<BaseNode> param;

    public DotNode(TokenType dotType, List<BaseNode> param) {
        this.dotType = dotType;
        this.param = param;
    }

    @Override
    public BaseValue eval(Scope scope) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TokenType getOperationType() {
        return TokenType.DOT;
    }

    @Override
    public void putOperation(Operation operation) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void putOperationValue(BaseValue value) {
        // TODO Auto-generated method stub

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
    
}
