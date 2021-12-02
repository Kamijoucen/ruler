package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.CallLinkNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class AssignEval implements BaseEval<AssignNode> {
    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        CallLinkNode leftNode = (CallLinkNode) node.getLeftNode();
        int callLength = leftNode.getCalls().size();
        if (callLength == 0) {
            NameNode name = (NameNode) leftNode.getFirst();
            BaseValue expBaseValue = node.getExpression().eval(context, scope);
            scope.update(name.name.name, expBaseValue);
            return NoneValue.INSTANCE;
        } else {
            leftNode.evalAssign(context, scope);
            OperationNode lastNode = CollectionUtil.last(leftNode.getCalls());
            assert lastNode != null;
            lastNode.assign(node.getExpression(), scope, context);
        }
        return NoneValue.INSTANCE;
    }
}
