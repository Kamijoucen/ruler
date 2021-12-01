package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

import java.util.List;

public class DefaultStatementVisitor implements StatementVisitor {
    @Override
    public BaseValue eval(CallLinkNode node, boolean isAssign, Scope scope, RuntimeContext context) {
        BaseValue statementValue = node.getFirst().eval(context, scope);
        List<OperationNode> calls = node.getCalls();
        int length = node.getCalls().size();
        if (isAssign) {
            length--;
        }
        for (int i = 0; i < length; i++) {
            scope.putCallLinkPreviousValue(statementValue);
            statementValue = calls.get(i).eval(context, scope);
        }
        if (isAssign) {
            scope.putCallLinkPreviousValue(statementValue);
        }
        return statementValue;
    }
}
