package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.operation.CustomOperation;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;

public class BinaryOperationEval implements BaseEval<BinaryOperationNode> {

    @Override
    public BaseValue eval(BinaryOperationNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        BinaryOperation operation = node.getOperation();
        if (operation == null) {
            throw new RuntimeException("Operation not supported: " + node.getOpName());
        }
        if (operation instanceof CustomOperation) {
            ClosureValue fun = context.getInfixOperation(node.getOpName());
            if (fun == null) {
                throw SyntaxException.withSyntax("Custom infix not found: '" + node.getOpName() + "'");
            }
            return operation.invoke(node.getLhs(), node.getRhs(), env, context, fun);
        }
        return operation.invoke(node.getLhs(), node.getRhs(), env, context);
    }
}
