package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.IntegerNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class IntegerEval implements BaseEval<IntegerNode> {

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return context.getConfiguration().getIntegerNumberCache().getValue(node.getValue());
    }
}
