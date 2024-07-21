package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.IntegerNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class IntegerEval implements BaseEval<IntegerNode> {

    @Override
    public BaseValue eval(IntegerNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        return context.getConfiguration().getIntegerNumberCache().getValue(node.getValue());
    }
}
