package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.domain.ast.BoolNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;

public class BooleanEval implements BaseEval<BoolNode> {
    @Override
    public BaseValue eval(BoolNode node, Scope scope, RuntimeContext context) {
        return BoolValue.get(node.getValue());
    }
}
