package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.ContinueNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class ContinueEval implements BaseEval<ContinueNode> {
    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        context.setContinueFlag(true);
        return NullValue.INSTANCE;
    }
}
