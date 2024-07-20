package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class OutNameNode extends AbstractBaseNode {

    public final Token name;

    public OutNameNode(Token name, TokenLocation location) {
        super(location);
        this.name = name;
    }
    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

}
