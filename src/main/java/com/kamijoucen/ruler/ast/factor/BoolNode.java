package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class BoolNode extends AbstractBaseNode {

    private boolean value;

    public BoolNode(boolean value, TokenLocation location) {
        super(location);
        this.value = value;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
