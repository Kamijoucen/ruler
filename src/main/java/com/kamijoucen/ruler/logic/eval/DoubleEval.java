package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.DoubleNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DoubleValue;

public class DoubleEval implements BaseEval<DoubleNode> {
    @Override
    public BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return new DoubleValue(node.getValue());
    }
}
