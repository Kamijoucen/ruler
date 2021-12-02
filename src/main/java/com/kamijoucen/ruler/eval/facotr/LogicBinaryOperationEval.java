package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.LogicBinaryOperationNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class LogicBinaryOperationEval implements BaseEval<LogicBinaryOperationNode> {
    @Override
    public BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseNode exp1 = node.getExp1();
        BaseNode exp2 = node.getExp2();
        return node.getLogicOperation().compute(context, scope, exp1, exp2);
    }
}
