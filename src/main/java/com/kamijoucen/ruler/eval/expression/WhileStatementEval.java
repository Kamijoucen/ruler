package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.QuadConsumer;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * while循环语句求值器
 *
 * @author Kamijoucen
 */
public class WhileStatementEval implements BaseEval<WhileStatementNode> {

    private static final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> CHECK_LOOP_NUMBER_EVAL =
            LoopCountCheckOperation::accept;

    private static final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> BLANK_EVAL =
            (operation, node, scope, context) -> {
                // 空操作，当不需要检查循环次数时使用
            };

    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseNode block = node.getBlock();

        // 初始化循环计数检查
        LoopCountCheckOperation loopCountCheckOperation = null;
        QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> check;

        if (context.getConfiguration().getMaxLoopNumber() > 0) {
            loopCountCheckOperation = context.getConfiguration().getRuntimeBehaviorFactory()
                    .createLoopCountCheckOperation();
            check = CHECK_LOOP_NUMBER_EVAL;
        } else {
            check = BLANK_EVAL;
        }

        BaseValue lastValue = NullValue.INSTANCE;

        // 执行循环
        while (true) {
            // 计算条件表达式
            BaseValue conditionValue = node.getCondition().eval(scope, context);

            // 类型检查和转换
            if (conditionValue.getType() != ValueType.BOOL) {
                throw TypeException.typeMismatch(ValueType.BOOL, conditionValue.getType(),
                        node.getCondition().getLocation());
            }

            // 检查循环条件
            if (!((BoolValue) conditionValue).getValue()) {
                break;
            }

            // 检查循环次数限制
            check.accept(loopCountCheckOperation, node, scope, context);

            // 执行循环体
            lastValue = block.eval(scope, context);

            // 处理控制流
            if (context.isReturnFlag()) {
                // return语句，直接退出
                break;
            } else if (context.isBreakFlag()) {
                // break语句，清除标志并退出循环
                context.setBreakFlag(false);
                break;
            } else if (context.isContinueFlag()) {
                // continue语句，清除标志并继续下一次循环
                context.setContinueFlag(false);
                // continue会自动进入下一次循环
            }
        }

        return lastValue;
    }
}
