package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class IndexEval implements BaseEval<IndexNode> {
    @Override
    public BaseValue eval(IndexNode node, Environment env, RuntimeContext context,
            NodeVisitor visitor) {
        return node.getOperation().invoke(node.getLhs(), node.getRhs(), env, context, visitor);
    }
}
