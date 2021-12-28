package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.facotr.OutNameNode;
import com.kamijoucen.ruler.common.AbstractVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class OutNameVisitor extends AbstractVisitor {
    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        context.getOutNameToken().add(node.name);
        return super.eval(node, scope, context);
    }
}
