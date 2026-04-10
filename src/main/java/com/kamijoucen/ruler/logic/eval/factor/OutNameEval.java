package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.OutNameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

public class OutNameEval implements BaseEval<OutNameNode> {

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        BaseValue value = context.findOutValue(node.name.name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }
}
