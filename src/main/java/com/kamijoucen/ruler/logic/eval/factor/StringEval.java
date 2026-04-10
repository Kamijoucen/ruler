package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.StringNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class StringEval implements BaseEval<StringNode> {
    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return new StringValue(node.getValue());
    }

}
