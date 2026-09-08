package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ArrayNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;

import java.util.ArrayList;
import java.util.List;

public class ArrayEval implements BaseEval<ArrayNode> {
    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> nodes = node.getValues();
        if (nodes.isEmpty()) {
            return new ArrayValue(new ArrayList<>());
        }
        List<BaseValue> values = new ArrayList<>(nodes.size());
        for (BaseNode tempNode : nodes) {
            values.add(EvalVisitor.evaluate(tempNode, scope, context));
        }
        return new ArrayValue(values);
    }

}
