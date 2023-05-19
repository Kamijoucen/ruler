package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.BoolNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BoolValue;

public class BooleanEval implements BaseEval<BoolNode> {
    @Override
    public EvalResult eval(BoolNode node, Scope scope, RuntimeContext context) {
        return BoolValue.get(node.getValue());
    }
}
