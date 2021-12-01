package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.BoolNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;

public class BooleanEval implements BaseEval<BoolNode> {
    @Override
    public BaseValue eval(BoolNode node, Scope scope, RuntimeContext context) {
        return BoolValue.get(node.getValue());
    }
}
