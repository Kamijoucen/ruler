package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.domain.ast.UnaryOperationNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class UnaryOperationEval implements BaseEval<UnaryOperationNode> {
    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue value = node.getExp().eval(scope, context);
        return node.getOperation().invoke(null, null, scope, context, value);
    }
}
