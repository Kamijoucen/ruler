package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.StringInterpolationNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.StringValue;

import java.util.List;

public class StringInterpolationEval implements BaseEval<StringInterpolationNode> {
    @Override
    public BaseValue eval(StringInterpolationNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> parts = node.getParts();
        StringBuilder sb = new StringBuilder();
        for (BaseNode part : parts) {
            BaseValue value = EvalVisitor.evaluate(part, scope, context);
            sb.append(value == null ? "null" : value.toString());
        }
        return new StringValue(sb.toString());
    }
}
