package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.RsonNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.RsonValue;

import java.util.Map;

public class RsonEval implements BaseEval<RsonNode> {
    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {
        MataData mataData = new MataData();
        for (Map.Entry<String, BaseNode> entry : node.getProperties().entrySet()) {
            String name = entry.getKey();
            BaseValue value = entry.getValue().eval(context, scope);
            mataData.put(name, value);
        }
        return new RsonValue(mataData);
    }
}
