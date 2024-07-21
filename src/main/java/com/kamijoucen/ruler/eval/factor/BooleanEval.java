package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.BoolNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;

public class BooleanEval implements BaseEval<BoolNode> {
    @Override
    public BaseValue eval(BoolNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        return BoolValue.get(node.getValue());
    }
}
