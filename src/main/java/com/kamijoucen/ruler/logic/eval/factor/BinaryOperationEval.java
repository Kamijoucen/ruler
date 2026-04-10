package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;
import com.kamijoucen.ruler.logic.operation.CustomOperation;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;

public class BinaryOperationEval implements BaseEval<BinaryOperationNode> {

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BinaryOperation operation = node.getOperation();
        if (operation == null) {
            throw new RuntimeException("Operation not supported: " + node.getOpName());
        }
        if (operation instanceof CustomOperation) {
            ClosureValue fun = context.getInfixOperation(node.getOpName());
            if (fun == null) {
                throw SyntaxException.withSyntax("Custom infix not found: '" + node.getOpName() + "'");
            }
            return operation.invoke(node.getLhs(), node.getRhs(), scope, context, fun);
        }
        return operation.invoke(node.getLhs(), node.getRhs(), scope, context);
    }
}
