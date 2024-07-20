package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.NullNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class NullEval implements BaseEval<NullNode> {
    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        return NullValue.INSTANCE;
    }
}
