package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.RsonNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.RsonValue;

import java.util.HashMap;
import java.util.Map;

public class RsonEval implements BaseEval<RsonNode> {

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {

        Map<String, BaseValue> fields = new HashMap<>();
        for (Map.Entry<String, BaseNode> entry : node.getProperties().entrySet()) {
            String name = entry.getKey();
            BaseValue value = EvalVisitor.evaluate(entry.getValue(), scope, context);
            fields.put(name, value);
        }
        return new RsonValue(fields);
    }
}
