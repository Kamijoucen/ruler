package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class VirtualNode implements BaseNode {

    private final BaseValue baseValue;

    public VirtualNode(BaseValue baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    @Override
    public TokenLocation getLocation() {
        throw new UnsupportedOperationException();
    }

    public BaseValue getBaseValue() {
        return baseValue;
    }
}
