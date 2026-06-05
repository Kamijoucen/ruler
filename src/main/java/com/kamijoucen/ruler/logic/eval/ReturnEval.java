package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.ReturnNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

import java.util.ArrayList;
import java.util.List;

public class ReturnEval implements BaseEval<ReturnNode> {

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        List<BaseValue> values = new ArrayList<>(param.size());
        for (BaseNode baseNode : param) {
            values.add(baseNode.eval(scope, context));
        }
        context.setReturnFlag(true);
        context.setReturnSpace(values);
        return NullValue.INSTANCE;
    }
}
