package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.ContinueNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.constant.ContinueValue;

public class ContinueEval implements BaseEval<ContinueNode> {
    @Override
    public EvalResult eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return ContinueValue.INSTANCE;
    }
}
