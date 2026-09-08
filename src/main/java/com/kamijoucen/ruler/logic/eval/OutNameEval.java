package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.OutNameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

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
