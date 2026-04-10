package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.BreakNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

public class BreakEval implements BaseEval<BreakNode> {
    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        context.setBreakFlag(true);
        return NullValue.INSTANCE;
    }
}
