package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.expression.IndexNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class IndexEval implements BaseEval<IndexNode> {
    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        return node.getOperation().invoke(node.getLhs(), node.getRhs(), scope, context);
    }
}
