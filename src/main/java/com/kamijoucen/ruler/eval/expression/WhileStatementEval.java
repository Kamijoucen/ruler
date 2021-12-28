package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NoneValue;
import com.kamijoucen.ruler.value.constant.ReturnValue;

public class WhileStatementEval implements BaseEval<WhileStatementNode> {
    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseNode block = node.getBlock();
        while (((BoolValue) node.getCondition().eval(context, scope)).getValue()) {
            BaseValue blockValue = block.eval(context, scope);
            if (ValueType.BREAK == blockValue.getType()) {
                break;
            } else if (ValueType.RETURN == blockValue.getType()) {
                return ReturnValue.INSTANCE;
            }
        }
        return NoneValue.INSTANCE;
    }
}
