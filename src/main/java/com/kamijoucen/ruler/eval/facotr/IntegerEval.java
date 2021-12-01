package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.IntegerNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;

public class IntegerEval implements BaseEval<IntegerNode> {

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return new IntegerValue(node.getValue());
    }
}
