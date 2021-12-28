package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.CallLinkNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallLinkEval implements BaseEval<CallLinkNode> {
    @Override
    public BaseValue eval(CallLinkNode node, Scope scope, RuntimeContext context) {
        BaseValue statementValue = node.getFirst().eval(context, scope);
        List<OperationNode> calls = node.getCalls();
        int length = node.getCalls().size();
        if (context.getCallLinkAssign()) {
            length--;
        }
        for (int i = 0; i < length; i++) {
            scope.putCallLinkPreviousValue(statementValue);
            statementValue = calls.get(i).eval(context, scope);
        }
        if (context.getCallLinkAssign()) {
            scope.putCallLinkPreviousValue(statementValue);
        }
        return statementValue;
    }
}
