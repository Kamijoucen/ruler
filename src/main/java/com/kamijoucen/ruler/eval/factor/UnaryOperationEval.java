package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.UnaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryOperationEval implements BaseEval<UnaryOperationNode> {
    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue value = node.getExp().eval(scope, context);
        return node.getOperation().invoke(null, null, scope, context, value);
    }
}
