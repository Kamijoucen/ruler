package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.List;

/**
 * foreach循环语句求值器
 *
 * @author Kamijoucen
 */
public class ForEachStatementEval implements BaseEval<ForEachStatementNode> {

        @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        // 计算被迭代的表达式
        BaseValue iterableValue = node.getList().eval(scope, context);

        // 类型检查
        if (iterableValue.getType() != ValueType.ARRAY) {
            throw TypeException.typeMismatch(ValueType.ARRAY, iterableValue.getType(),
                    node.getList().getLocation());
        }

        ArrayValue array = (ArrayValue) iterableValue;
        List<BaseValue> values = array.getValues();
        BaseNode block = node.getBlock();

        // 创建循环作用域
        Scope loopScope = new Scope("foreach", false, scope, node.getLocation());

        // 定义循环变量
        Token loopName = node.getLoopName();
        loopScope.defineLocal(loopName.name, NullValue.INSTANCE);

        BaseValue lastValue = NullValue.INSTANCE;

        // 遍历数组
        for (BaseValue value : values) {
            // 更新循环变量的值
            loopScope.update(loopName.name, value);

            // 执行循环体
            lastValue = block.eval(loopScope, context);

            // 处理控制流
            if (context.isReturnFlag()) {
                break;
            } else if (context.isBreakFlag()) {
                context.setBreakFlag(false);
                break;
            } else if (context.isContinueFlag()) {
                context.setContinueFlag(false);
                // continue会自动进入下一次循环
            }
        }

        return lastValue;
    }
}
