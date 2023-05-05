package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.CallChainNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallChainEval implements BaseEval<CallChainNode> {

    @Override
    public BaseValue eval(CallChainNode node, Scope scope, RuntimeContext context) {
        BaseValue statementValue = node.getFirst().eval(context, scope);
        List<OperationNode> calls = node.getCalls();
        int length = node.getCalls().size();
        if (context.getCallChainAssign()) {
            length--;
        }
        for (int i = 0; i < length; i++) {
            scope.putCallChainPreviousValue(statementValue);
            statementValue = calls.get(i).eval(scope, context);
        }
        if (context.getCallChainAssign()) {
            scope.putCallChainPreviousValue(statementValue);
        }
        return statementValue;
    }
}
