package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * if语句求值器
 *
 * @author Kamijoucen
 */
public class IfStatementEval implements BaseEval<IfStatementNode> {

    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue conditionValue = node.getCondition().eval(scope, context);

        // 类型检查
        if (conditionValue.getType() != ValueType.BOOL) {
            throw TypeException.typeMismatch(ValueType.BOOL, conditionValue.getType(),
                    node.getCondition().getLocation());
        }

        BoolValue condition = (BoolValue) conditionValue;

        if (condition.getValue()) {
            // 执行then分支
            return node.getThenBlock().eval(scope, context);
        } else if (node.getElseBlock() != null) {
            // 执行else分支
            return node.getElseBlock().eval(scope, context);
        } else {
            // 没有else分支，返回null
            return NullValue.INSTANCE;
        }
    }
}
