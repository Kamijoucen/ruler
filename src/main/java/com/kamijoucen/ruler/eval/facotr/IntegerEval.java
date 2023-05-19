package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.IntegerNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;

public class IntegerEval implements BaseEval<IntegerNode> {

    @Override
    public EvalResult eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return context.getConfiguration().getIntegerNumberCache().getValue(node.getValue());
    }
}
