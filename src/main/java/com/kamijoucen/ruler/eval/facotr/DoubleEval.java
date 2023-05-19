package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.DoubleNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.DoubleValue;

public class DoubleEval implements BaseEval<DoubleNode> {
    @Override
    public EvalResult eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return new DoubleValue(node.getValue());
    }
}
