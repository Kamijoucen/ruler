package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.DefaultParamValNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class DefaultParamValEval implements BaseEval<DefaultParamValNode> {

    @Override
    public BaseValue eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        String paramName = node.getName().name.name;
        BaseValue defValue = EvalVisitor.evaluate(node.getExp(), scope, context);
        scope.putLocal(paramName, defValue);
        return null;
    }

}
