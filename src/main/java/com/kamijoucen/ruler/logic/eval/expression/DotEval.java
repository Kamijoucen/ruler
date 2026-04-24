package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.DotNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import com.kamijoucen.ruler.domain.value.FunctionValue;
import com.kamijoucen.ruler.domain.value.MethodValue;
import com.kamijoucen.ruler.domain.value.ModuleValue;
import com.kamijoucen.ruler.logic.property.PropertyAccessor;

public class DotEval implements BaseEval<DotNode> {

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        BaseValue prevValue = node.getLhs().eval(scope, context);

        BaseNode nodeName = node.getRhs();
        if (!(nodeName instanceof NameNode)) {
            throw new RulerRuntimeException("dot expression rhs must be an identifier");
        }
        String callName = ((NameNode) nodeName).name.name;
        BaseValue callValue = PropertyAccessor.getProperty(prevValue, callName, context);
        if ((callValue instanceof ClosureValue || callValue instanceof FunctionValue)
                && !(prevValue instanceof ModuleValue)) {
            return new MethodValue(callValue, prevValue);
        }
        return callValue;
    }

}
