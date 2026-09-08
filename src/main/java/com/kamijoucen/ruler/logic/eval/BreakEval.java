package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BreakNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

public class BreakEval implements BaseEval<BreakNode> {
    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        context.setBreakFlag(true);
        return NullValue.INSTANCE;
    }
}
