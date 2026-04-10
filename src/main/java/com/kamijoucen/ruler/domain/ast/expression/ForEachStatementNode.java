package com.kamijoucen.ruler.domain.ast.expression;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
