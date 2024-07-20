package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class DoubleNode extends AbstractBaseNode {

    public double value;

    public DoubleNode(double value, TokenLocation location) {
        super(location);
        this.value = value;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
