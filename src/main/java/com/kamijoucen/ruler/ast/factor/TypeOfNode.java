package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class TypeOfNode extends AbstractBaseNode {

    private BaseNode exp;

    public TypeOfNode(BaseNode exp, TokenLocation location) {
        super(location);
        this.exp = exp;
    }
    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public BaseNode getExp() {
        return exp;
    }

    public void setExp(BaseNode exp) {
        this.exp = exp;
    }
}
