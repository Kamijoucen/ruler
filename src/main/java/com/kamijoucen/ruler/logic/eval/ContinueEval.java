package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.ContinueNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

public class ContinueEval implements BaseEval<ContinueNode> {
    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        context.setContinueFlag(true);
        return NullValue.INSTANCE;
    }
}
