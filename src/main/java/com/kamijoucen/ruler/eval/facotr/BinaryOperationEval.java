package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.operation.CustomOperation;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;

public class BinaryOperationEval implements BaseEval<BinaryOperationNode> {
    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue val1 = node.getExp1().eval(context, scope);
        BaseValue val2 = node.getExp2().eval(context, scope);

        Operation operation = node.getOperation();
        if (operation == null) {
            throw new RuntimeException("not support operation: " + node.getOperationName());
        }

        if (operation instanceof CustomOperation) {
            ClosureValue fun = context.getInfixOperation(node.getOperationName());
            return operation.compute(context, fun, val1, val2);
        }

        return operation.compute(context, val1, val2);
    }
}
