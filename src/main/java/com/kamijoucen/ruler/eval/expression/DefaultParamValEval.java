package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class DefaultParamValEval implements BaseEval<DefaultParamValNode> {

    @Override
    public BaseValue eval(DefaultParamValNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        String paramName = node.getName().name.name;
        BaseValue defValue = node.getExp().eval(visitor);
        scope.putLocal(paramName, defValue);
        return null;
    }

}
