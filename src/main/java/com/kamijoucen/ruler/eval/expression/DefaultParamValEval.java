package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.DefaultParamValueNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class DefaultParamValEval implements BaseEval<DefaultParamValueNode> {

    @Override
    public BaseValue eval(DefaultParamValueNode node, Scope scope, RuntimeContext context) {
        String paramName = node.getName().name.name;
        BaseValue defValue = node.getExp().eval(scope, context);
        scope.putLocal(paramName, defValue);
        return null;
    }

}
