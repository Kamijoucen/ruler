package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryOperationNode extends AbstractBaseNode {

    private TokenType op;

    private BaseNode exp;

    private Operation operation;

    public UnaryOperationNode(TokenType op, BaseNode exp, Operation operation) {
        this.op = op;
        this.exp = exp;
        this.operation = operation;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    public TokenType getOp() {
        return op;
    }

    public void setOp(TokenType op) {
        this.op = op;
    }

    public BaseNode getExp() {
        return exp;
    }

    public void setExp(BaseNode exp) {
        this.exp = exp;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
