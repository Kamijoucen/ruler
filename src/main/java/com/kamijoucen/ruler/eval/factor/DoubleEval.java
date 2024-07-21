package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.DoubleNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;

public class DoubleEval implements BaseEval<DoubleNode> {
    @Override
    public BaseValue eval(DoubleNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        return new DoubleValue(node.getValue());
    }
}
