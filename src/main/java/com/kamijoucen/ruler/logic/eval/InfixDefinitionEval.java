package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.expression.InfixDefinitionNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import com.kamijoucen.ruler.domain.value.NullValue;

public class InfixDefinitionEval implements BaseEval<InfixDefinitionNode> {

    @Override
    public BaseValue eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        String functionName = node.getFunction().getName();
        BaseValue functionValue = node.getFunction().eval(scope, context);
        context.addInfixOperation(functionName, (ClosureValue) functionValue);
        return NullValue.INSTANCE;
    }

}
