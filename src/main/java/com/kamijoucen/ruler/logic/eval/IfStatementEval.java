package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.IfStatementNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class IfStatementEval implements BaseEval<IfStatementNode> {
    
    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue conditionValue = EvalVisitor.evaluate(node.getCondition(), scope, context);
        if (conditionValue.getType() != ValueType.BOOL) {
            throw new RulerRuntimeException("if condition must be boolean",
                    node.getCondition().getLocation());
        }
        BoolValue boolValue = (BoolValue) conditionValue;
        if (boolValue.getValue()) {
            BaseNode thenBlock = node.getThenBlock();
            return EvalVisitor.evaluate(thenBlock, scope, context);
        } else {
            BaseNode elseBlock = node.getElseBlock();
            if (elseBlock != null) {
                return EvalVisitor.evaluate(elseBlock, scope, context);
            }
        }
        return NullValue.INSTANCE;
    }
}
