package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.ast.IntegerNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class IntegerEval implements BaseEval<IntegerNode> {

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return IntegerValue.valueOf(node.getValue());
    }
}
