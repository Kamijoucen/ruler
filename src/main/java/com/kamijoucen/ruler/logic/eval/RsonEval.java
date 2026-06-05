package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.RsonNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.RsonValue;

import java.util.HashMap;
import java.util.Map;

public class RsonEval implements BaseEval<RsonNode> {

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {

        Map<String, BaseValue> fields = new HashMap<>();
        for (Map.Entry<String, BaseNode> entry : node.getProperties().entrySet()) {
            String name = entry.getKey();
            BaseValue value = entry.getValue().eval(scope, context);
            fields.put(name, value);
        }
        return new RsonValue(fields);
    }
}
