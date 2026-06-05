package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.StringInterpolationNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

import java.util.List;

public class StringInterpolationEval implements BaseEval<StringInterpolationNode> {
    @Override
    public BaseValue eval(StringInterpolationNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> parts = node.getParts();
        StringBuilder sb = new StringBuilder();
        for (BaseNode part : parts) {
            BaseValue value = part.eval(scope, context);
            sb.append(value == null ? "null" : value.toString());
        }
        return new StringValue(sb.toString());
    }
}
