package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.BreakNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.BreakValue;

public class BreakEval implements BaseEval<BreakNode> {
    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        return BreakValue.INSTANCE;
    }
}
