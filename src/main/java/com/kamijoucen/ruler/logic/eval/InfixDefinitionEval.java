package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.InfixDefinitionNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ClosureValue;
import com.kamijoucen.ruler.types.value.NullValue;

public class InfixDefinitionEval implements BaseEval<InfixDefinitionNode> {

    @Override
    public BaseValue eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        String functionName = node.getFunction().getName();
        BaseValue functionValue = EvalVisitor.evaluate(node.getFunction(), scope, context);
        context.addInfixOperation(functionName, (ClosureValue) functionValue);
        return NullValue.INSTANCE;
    }

}
