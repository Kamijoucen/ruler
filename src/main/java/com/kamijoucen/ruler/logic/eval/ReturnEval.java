package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ReturnNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

import java.util.ArrayList;
import java.util.List;

public class ReturnEval implements BaseEval<ReturnNode> {

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        List<BaseValue> values = new ArrayList<>(param.size());
        for (BaseNode baseNode : param) {
            values.add(EvalVisitor.evaluate(baseNode, scope, context));
        }
        context.startReturn(values);
        return NullValue.INSTANCE;
    }
}
