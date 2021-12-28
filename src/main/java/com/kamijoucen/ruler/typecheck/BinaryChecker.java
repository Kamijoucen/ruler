package com.kamijoucen.ruler.typecheck;

import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class BinaryChecker implements BaseEval<BinaryOperationNode> {

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue val1 = node.getExp1().eval(context, scope);
        BaseValue val2 = node.getExp2().eval(context, scope);
        TokenType op = node.getOp();
        if (op == TokenType.ADD || op == TokenType.SUB
                || op == TokenType.MUL || op == TokenType.DIV) {

        } else if (op == TokenType.AND || op == TokenType.OR) {

        }
        return null;
    }

}
