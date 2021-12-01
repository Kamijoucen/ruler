package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.ArrayNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.List;

public class ArrayEval implements BaseEval<ArrayNode> {
    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> nodes = node.getValues();
        if (nodes.size() == 0) {
            return new ArrayValue(new ArrayList<BaseValue>());
        }
        List<BaseValue> values = new ArrayList<BaseValue>(nodes.size());
        for (BaseNode tempNode : nodes) {
            values.add(tempNode.eval(context, scope));
        }
        return new ArrayValue(values);
    }
}
