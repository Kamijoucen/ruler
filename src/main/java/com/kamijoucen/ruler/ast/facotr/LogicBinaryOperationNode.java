package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.LogicOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class LogicBinaryOperationNode extends AbstractBaseNode {

    private BaseNode exp1;
    private BaseNode exp2;
    private TokenType op;
    private LogicOperation logicOperation;

    public LogicBinaryOperationNode(TokenType op, BaseNode exp1, BaseNode exp2, LogicOperation logicOperation) {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.logicOperation = logicOperation;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(RuntimeContext context, Scope scope) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public BaseNode getExp1() {
        return exp1;
    }

    public void setExp1(BaseNode exp1) {
        this.exp1 = exp1;
    }

    public BaseNode getExp2() {
        return exp2;
    }

    public void setExp2(BaseNode exp2) {
        this.exp2 = exp2;
    }

    public TokenType getOp() {
        return op;
    }

    public void setOp(TokenType op) {
        this.op = op;
    }

    public LogicOperation getLogicOperation() {
        return logicOperation;
    }

    public void setLogicOperation(LogicOperation logicOperation) {
        this.logicOperation = logicOperation;
    }
}
