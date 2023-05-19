package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.NullNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.constant.NullValue;

public class NullEval implements BaseEval<NullNode> {
    @Override
    public EvalResult eval(NullNode node, Scope scope, RuntimeContext context) {
        return NullValue.INSTANCE;
    }
}
