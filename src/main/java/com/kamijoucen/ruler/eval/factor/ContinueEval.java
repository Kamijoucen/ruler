package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.ContinueNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class ContinueEval implements BaseEval<ContinueNode> {
    @Override
    public BaseValue eval(ContinueNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        context.setContinueFlag(true);
        return NullValue.INSTANCE;
    }
}
