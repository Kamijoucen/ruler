package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class DefaultParamValEval implements BaseEval<DefaultParamValNode> {

    @Override
    public BaseValue eval(DefaultParamValNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        String paramName = node.getName().name.name;
        BaseValue defValue = node.getExp().eval(scope, context);
        scope.putLocal(paramName, defValue);
        return null;
    }

}
