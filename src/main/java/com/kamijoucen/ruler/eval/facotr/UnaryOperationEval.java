package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.UnaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryOperationEval implements BaseEval<UnaryOperationNode> {
    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        return node.getOperation().invoke(context, , node.getExp().eval(scope, context));
    }
}
