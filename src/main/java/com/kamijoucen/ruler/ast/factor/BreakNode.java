package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class BreakNode extends AbstractBaseNode {

    public BreakNode(TokenLocation location) {
        super(location);
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

}
