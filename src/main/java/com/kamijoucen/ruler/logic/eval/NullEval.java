package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.domain.ast.NullNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

public class NullEval implements BaseEval<NullNode> {
    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return NullValue.INSTANCE;
    }
}
