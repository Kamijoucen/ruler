package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallEval implements BaseEval<CallNode> {

    @Override
    public BaseValue eval(CallNode node, Environment env, RuntimeContext context,
            NodeVisitor visitor) {
        List<BaseNode> callParams = node.getParams();
        BaseValue[] invokeParams = new BaseValue[callParams.size()];
        for (int i = 0; i < callParams.size(); i++) {
            invokeParams[i] = callParams.get(i).eval(visitor);
        }
        BaseValue returnValue = node.getOperation().invoke(node.getLhs(), null, env, context,
                visitor, invokeParams);
        context.setReturnFlag(false);
        context.clearReturnSpace();
        return returnValue;
    }
}
