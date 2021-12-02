package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ThisNode extends AbstractBaseNode {

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

}