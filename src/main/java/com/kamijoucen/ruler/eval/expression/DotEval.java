package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.MethodValue;
import com.kamijoucen.ruler.value.ModuleValue;

public class DotEval implements BaseEval<DotNode> {

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        BaseValue prevValue = node.getLhs().eval(scope, context);

        BaseNode nodeName = node.getRhs();
        if (!(nodeName instanceof NameNode)) {
            throw new IllegalArgumentException();
        }
        String callName = ((NameNode) nodeName).name.name;
        BaseValue callValue = context.getConfiguration().getObjectAccessControlManager().accessObject(prevValue,
                callName, context);
        if ((callValue instanceof ClosureValue || callValue instanceof FunctionValue)
                && !(prevValue instanceof ModuleValue)) {
            return new MethodValue(callValue, prevValue);
        }
        return callValue;
    }

}
