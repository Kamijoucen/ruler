package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class DefaultParamValNode extends AbstractBaseNode {

    private final NameNode name;
    private final BaseNode exp;

    public DefaultParamValNode(NameNode name, BaseNode exp, TokenLocation location) {
        super(location);
        this.name = name;
        this.exp = exp;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public NameNode getName() {
        return name;
    }


    public BaseNode getExp() {
        return exp;
    }

}
