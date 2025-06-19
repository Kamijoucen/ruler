package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.operation.CustomOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;

/**
 * 二元操作求值器
 *
 * @author Kamijoucen
 */
public class BinaryOperationEval implements BaseEval<BinaryOperationNode> {

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BinaryOperation operation = node.getOperation();
        if (operation == null) {
            throw new SyntaxException("不支持的操作符: '" + node.getOpName() + "'", node.getLocation());
        }
        if (operation instanceof CustomOperation) {
            ClosureValue fun = context.getInfixOperation(node.getOpName());
            if (fun == null) {
                throw new SyntaxException("未找到自定义中缀操作符: '" + node.getOpName() + "'",
                        node.getLocation());
            }
            return operation.invoke(node.getLhs(), node.getRhs(), scope, context, fun);
        }
        return operation.invoke(node.getLhs(), node.getRhs(), scope, context);
    }
}
