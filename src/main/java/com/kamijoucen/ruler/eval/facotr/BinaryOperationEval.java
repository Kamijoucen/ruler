package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class BinaryOperationEval implements BaseEval<BinaryOperationNode> {
    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue val1 = node.getExp1().eval(context, scope);
        BaseValue val2 = node.getExp2().eval(context, scope);
        return node.getOperation().compute(context, val1, val2);
    }
}
