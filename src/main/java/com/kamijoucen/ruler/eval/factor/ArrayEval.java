package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.ArrayNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.List;

public class ArrayEval implements BaseEval<ArrayNode> {
    @Override
    public BaseValue eval(ArrayNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        List<BaseNode> nodes = node.getValues();
        if (nodes.isEmpty()) {
            return new ArrayValue(new ArrayList<>());
        }
        List<BaseValue> values = new ArrayList<>(nodes.size());
        for (BaseNode tempNode : nodes) {
            values.add(tempNode.eval(visitor));
        }
        return new ArrayValue(values);
    }

}
