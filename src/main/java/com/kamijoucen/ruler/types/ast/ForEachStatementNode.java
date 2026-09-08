package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenLocation;

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
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
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
