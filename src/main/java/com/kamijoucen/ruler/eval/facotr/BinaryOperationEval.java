package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.operation.CustomOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ClosureValue;

public class BinaryOperationEval implements BaseEval<BinaryOperationNode> {

    @Override
    public EvalResult eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BinaryOperation operation = node.getOperation();
        if (operation == null) {
            throw new RuntimeException("not support operation: " + node.getOpName());
        }
        if (operation instanceof CustomOperation) {
            ClosureValue fun = context.getInfixOperation(node.getOpName());
            if (fun == null) {
                throw SyntaxException.withSyntax("custom infix not found: '" + node.getOpName() + "'", node.getLocation());
            }
            return EvalResult.value(operation.invoke(node.getLhs(), node.getRhs(), scope, context, fun));
        }
        return EvalResult.value(operation.invoke(node.getLhs(), node.getRhs(), scope, context));
    }
}
