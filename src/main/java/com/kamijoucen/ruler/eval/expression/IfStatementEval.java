package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.statement.IfStatementNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class IfStatementEval implements BaseEval<IfStatementNode> {
    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue conditionValue = node.getCondition().eval(context, scope);
        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }
        BoolValue boolValue = (BoolValue) conditionValue;
        if (boolValue.getValue()) {
            BaseNode thenBlock = node.getThenBlock();
            return thenBlock.eval(context, scope);
        } else {
            BaseNode elseBlock = node.getElseBlock();
            if (elseBlock != null) {
                return elseBlock.eval(context, scope);
            }
        }
        return NoneValue.INSTANCE;
    }
}
