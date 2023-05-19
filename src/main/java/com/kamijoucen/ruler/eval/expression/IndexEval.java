package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class IndexEval implements BaseEval<IndexNode> {
    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        return node.getOperation().invoke(node.getLhs(), node.getRhs(), scope, context);
    }
}
