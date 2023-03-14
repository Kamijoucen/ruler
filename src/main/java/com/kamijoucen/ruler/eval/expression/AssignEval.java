package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.CallChainNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;

public class AssignEval implements BaseEval<AssignNode> {
    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        CallChainNode leftNode = (CallChainNode) node.getLeftNode();
        int callLength = leftNode.getCalls().size();
        if (callLength == 0) {
            NameNode name = (NameNode) leftNode.getFirst();
            BaseValue expBaseValue = node.getExpression().eval(context, scope);
            scope.update(name.name.name, expBaseValue);
            return expBaseValue;
        } else {
            leftNode.evalAssign(context, scope);
            OperationNode lastNode = CollectionUtil.last(leftNode.getCalls());
            assert lastNode != null;
            return lastNode.assign(node.getExpression(), scope, context);
        }
    }
}
