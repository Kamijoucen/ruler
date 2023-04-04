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
        List<BaseNode> param = node.getParam();
        BaseValue[] paramVal = new BaseValue[param.size() + 2];
        paramVal[0] = null;
        paramVal[1] = scope.getCallChainPreviousValue();
        for (int i = 0; i < param.size(); i++) {
            paramVal[i + 2] = param.get(i).eval(context, scope);
        }
        return node.getOperation().compute(context, paramVal);
    }
}
