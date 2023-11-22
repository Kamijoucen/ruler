package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.ValueType;

public class IfStatementEval implements BaseEval<IfStatementNode> {
    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue conditionValue = node.getCondition().eval(scope, context);
        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
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
