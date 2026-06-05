package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.ArrayNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
            values.add(tempNode.eval(scope, context));
        }
        return new ArrayValue(values);
    }

}
