package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.ReturnNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.ReturnValue;

import java.util.ArrayList;
import java.util.List;

public class ReturnEval implements BaseEval<ReturnNode> {
    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        List<BaseValue> values = new ArrayList<BaseValue>(param.size());
        for (BaseNode baseNode : param) {
            values.add(baseNode.eval(context, scope));
        }
        scope.putReturnValues(values);
        return ReturnValue.INSTANCE;
    }
}
