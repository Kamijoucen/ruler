package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.OutNameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class OutNameEval implements BaseEval<OutNameNode> {

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        BaseValue value = context.findOutValue(node.name.name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }
}
