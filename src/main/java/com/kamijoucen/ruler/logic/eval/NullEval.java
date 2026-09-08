package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.NullNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

public class NullEval implements BaseEval<NullNode> {
    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return NullValue.INSTANCE;
    }
}
