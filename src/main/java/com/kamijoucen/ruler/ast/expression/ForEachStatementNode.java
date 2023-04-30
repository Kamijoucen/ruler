package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class ForEachStatementNode extends AbstractBaseNode {

    private Token loopName;
    private BaseNode list;
    private BaseNode block;

    public ForEachStatementNode(Token loopName, BaseNode list, BaseNode block, TokenLocation location) {
        super(location);
        this.loopName = loopName;
        this.list = list;
        this.block = block;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public Token getLoopName() {
        return loopName;
    }

    public void setLoopName(Token loopName) {
        this.loopName = loopName;
    }

    public BaseNode getList() {
        return list;
    }

    public void setList(BaseNode list) {
        this.list = list;
    }

    public BaseNode getBlock() {
        return block;
    }

    public void setBlock(BaseNode block) {
        this.block = block;
    }
}
