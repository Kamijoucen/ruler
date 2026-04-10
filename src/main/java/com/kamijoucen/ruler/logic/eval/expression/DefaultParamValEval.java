package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class DefaultParamValEval implements BaseEval<DefaultParamValNode> {

    @Override
    public BaseValue eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        String paramName = node.getName().name.name;
        BaseValue defValue = node.getExp().eval(scope, context);
        scope.putLocal(paramName, defValue);
        return null;
    }

}
