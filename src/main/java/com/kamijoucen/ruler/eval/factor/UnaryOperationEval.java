package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.UnaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryOperationEval implements BaseEval<UnaryOperationNode> {
    @Override
    public BaseValue eval(UnaryOperationNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        BaseValue value = node.getExp().eval(visitor);
        return node.getOperation().invoke(null, null, env, context, visitor, value);
    }
}
