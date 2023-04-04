package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.StringNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class StringEval implements BaseEval<StringNode> {
    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return new StringValue(node.getValue());
    }

}
