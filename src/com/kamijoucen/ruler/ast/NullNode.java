package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class NullNode implements BaseNode {

    public static final NullNode NULL_NODE = new NullNode();

    private NullNode() {
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

}
