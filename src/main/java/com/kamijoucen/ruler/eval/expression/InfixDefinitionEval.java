package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.InfixDefinitionNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.NullValue;

public class InfixDefinitionEval implements BaseEval<InfixDefinitionNode> {

    @Override
    public BaseValue eval(InfixDefinitionNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        String functionName = node.getFunction().getName();
        BaseValue functionValue = node.getFunction().eval(scope, context);
        context.addInfixOperation(functionName, (ClosureValue) functionValue);
        return NullValue.INSTANCE;
    }

}
