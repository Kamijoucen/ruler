package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class DotEval implements BaseEval<DotNode> {

    @Override
    public BaseValue eval(DotNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        BaseValue prevValue = node.getLhs().eval(visitor);
        context.setCurrentSelfValue(prevValue);

        BaseNode nodeName = node.getRhs();
        if (!(nodeName instanceof NameNode)) {
            throw new IllegalArgumentException();
        }
        String callName = ((NameNode) nodeName).name.name;
        BaseValue callValue = context.getConfiguration().getObjectAccessControlManager().accessObject(prevValue,
                callName, context);
        return callValue;
    }

}
