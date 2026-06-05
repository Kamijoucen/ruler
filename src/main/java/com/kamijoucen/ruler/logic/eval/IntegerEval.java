package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.domain.ast.IntegerNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class IntegerEval implements BaseEval<IntegerNode> {

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return context.getConfiguration().getIntegerNumberCache().getValue(node.getValue());
    }
}
