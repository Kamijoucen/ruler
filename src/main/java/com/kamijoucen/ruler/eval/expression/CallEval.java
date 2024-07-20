package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallEval implements BaseEval<CallNode> {

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        List<BaseNode> callParams = node.getParams();
        BaseValue[] invokeParams = new BaseValue[callParams.size()];
        for (int i = 0; i < callParams.size(); i++) {
            invokeParams[i] = callParams.get(i).eval(scope, context);
        }
        BaseValue returnValue = node.getOperation().invoke(node.getLhs(), null, scope, context, invokeParams);
        context.setReturnFlag(false);
        context.clearReturnSpace();
        return returnValue;
    }
}
