package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.UnaryOperationNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.logic.operation.UnarySubOperation;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class UnaryOperationEval implements BaseEval<UnaryOperationNode> {
    private static final UnarySubOperation NEGATE = new UnarySubOperation();
    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue value = EvalVisitor.evaluate(node.getExp(), scope, context);
        return node.getOp() == TokenType.ADD ? value
                : NEGATE.invoke(null, null, scope, context, value);
    }
}
