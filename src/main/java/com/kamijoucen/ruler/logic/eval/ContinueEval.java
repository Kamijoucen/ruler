package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.ContinueNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

public class ContinueEval implements BaseEval<ContinueNode> {
    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        context.setContinueFlag(true);
        return NullValue.INSTANCE;
    }
}
