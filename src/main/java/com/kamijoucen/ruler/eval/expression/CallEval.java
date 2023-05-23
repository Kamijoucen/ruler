package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallEval implements BaseEval<CallNode> {

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> callParams = node.getParams();
        BaseValue[] invokeParams = new BaseValue[callParams.size() + 1];
        invokeParams[0] = context.getCurrentSelfValue();
        for (int i = 0; i < callParams.size(); i++) {
            invokeParams[i + 1] = callParams.get(i).eval(scope, context);
        }
        return node.getOperation().invoke(node.getLhs(), node.getRhs(), scope, context, invokeParams);
    }
}
