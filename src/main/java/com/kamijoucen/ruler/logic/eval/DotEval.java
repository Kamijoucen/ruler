package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.DotNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ClosureValue;
import com.kamijoucen.ruler.types.value.FunctionValue;
import com.kamijoucen.ruler.types.value.MethodValue;
import com.kamijoucen.ruler.types.value.ModuleValue;
import com.kamijoucen.ruler.logic.property.PropertyAccessor;

public class DotEval implements BaseEval<DotNode> {

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        BaseValue prevValue = EvalVisitor.evaluate(node.getLhs(), scope, context);

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
