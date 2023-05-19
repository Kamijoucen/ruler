package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.List;

public class CallEval implements BaseEval<CallNode> {

    @Override
    public EvalResult eval(CallNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParams();
        BaseValue[] paramVal = new BaseValue[param.size() + 1];

        if (node.getLhs() instanceof BinaryOperationNode
                && ((BinaryOperationNode) node.getLhs()).getLhs() instanceof BinaryOperationNode) {

        } else {
            paramVal[0] = NullValue.INSTANCE;
        }
        for (int i = 0; i < param.size(); i++) {
            paramVal[i + 1] = param.get(i).eval(scope, context);
        }
        return node.getOperation().invoke(node.getLhs(), node.getRhs(), scope, context, paramVal);
    }
}
