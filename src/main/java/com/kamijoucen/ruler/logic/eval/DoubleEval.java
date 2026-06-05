package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.DoubleNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;

public class DoubleEval implements BaseEval<DoubleNode> {
    @Override
    public BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return new DoubleValue(node.getValue());
    }
}
