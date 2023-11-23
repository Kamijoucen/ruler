package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.ReturnNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.ArrayList;
import java.util.List;

public class ReturnEval implements BaseEval<ReturnNode> {
    
    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        List<BaseValue> values = new ArrayList<BaseValue>(param.size());
        for (BaseNode baseNode : param) {
            values.add(baseNode.eval(scope, context));
        }
        context.setReturnFlag(true);
        context.setReturnSpace(values);        
        return NullValue.INSTANCE;
    }
}
