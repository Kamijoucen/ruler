package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class InfixDefinitionNode extends AbstractBaseNode {

    private final ClosureDefineNode function;

    public InfixDefinitionNode(ClosureDefineNode functionNode, TokenLocation location) {
        super(location);
        this.function = functionNode;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }
    public ClosureDefineNode getFunction() {
        return function;
    }
}
