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
        List<BaseNode> param = node.getParams();
        BaseValue[] paramVal = new BaseValue[param.size() + 1];
        paramVal[0] = null;
        for (int i = 0; i < param.size(); i++) {
            paramVal[i + 1] = param.get(i).eval(scope, context);
        }
        return node.getOperation().invoke(node.getLhs(), node.getRhs(), scope, context, paramVal);
    }
}
