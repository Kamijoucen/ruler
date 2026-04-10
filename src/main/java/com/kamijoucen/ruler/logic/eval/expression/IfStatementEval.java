package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class IfStatementEval implements BaseEval<IfStatementNode> {
    
    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue conditionValue = node.getCondition().eval(scope, context);
        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The condition of the if statement must be a boolean type!");
        }
        BoolValue boolValue = (BoolValue) conditionValue;
        if (boolValue.getValue()) {
            BaseNode thenBlock = node.getThenBlock();
            return thenBlock.eval(scope, context);
        } else {
            BaseNode elseBlock = node.getElseBlock();
            if (elseBlock != null) {
                return elseBlock.eval(scope, context);
            }
        }
        return NullValue.INSTANCE;
    }
}
