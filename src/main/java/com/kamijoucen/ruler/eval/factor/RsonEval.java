package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.RsonNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.RsonValue;

import java.util.HashMap;
import java.util.Map;

public class RsonEval implements BaseEval<RsonNode> {

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {

        Map<String, BaseValue> fields = new HashMap<>();
        for (Map.Entry<String, BaseNode> entry : node.getProperties().entrySet()) {
            String name = entry.getKey();
            BaseValue value = entry.getValue().eval(scope, context);
            fields.put(name, value);
        }
        return new RsonValue(fields);
    }
}
