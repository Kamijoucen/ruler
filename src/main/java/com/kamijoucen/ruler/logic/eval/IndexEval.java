package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.logic.operation.Operations;
import com.kamijoucen.ruler.types.ast.IndexNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class IndexEval implements BaseEval<IndexNode> {
    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        return Operations.findOperation(node.getOp().name()).invoke(node.getLhs(), node.getRhs(), scope, context);
    }
}
