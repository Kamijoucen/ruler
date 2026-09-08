package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.logic.operation.Operations;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.CallNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

import java.util.List;

public class CallEval implements BaseEval<CallNode> {

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> callParams = node.getParams();
        BaseValue[] invokeParams = new BaseValue[callParams.size()];
        for (int i = 0; i < callParams.size(); i++) {
            invokeParams[i] = EvalVisitor.evaluate(callParams.get(i), scope, context);
        }
        return Operations.findOperation(node.getOp().name()).invoke(node.getLhs(), null, scope, context, invokeParams);
    }
}
