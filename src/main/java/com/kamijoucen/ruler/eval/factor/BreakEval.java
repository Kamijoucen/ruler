package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.BreakNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class BreakEval implements BaseEval<BreakNode> {
    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        context.setBreakFlag(true);
        return NullValue.INSTANCE;
    }
}
