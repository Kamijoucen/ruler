package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.List;

/**
 * 代码块求值器
 * 处理由花括号包围的语句块
 *
 * @author Kamijoucen
 */
public class BlockEval implements BaseEval<BlockNode> {

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        // 创建块作用域
        Scope blockScope = new Scope("block", false, scope, null);

        List<BaseNode> blocks = node.getBlocks();
        if (CollectionUtil.isEmpty(blocks)) {
            return NullValue.INSTANCE;
        }

        BaseValue lastVal = NullValue.INSTANCE;

        // 执行块中的每个语句
        for (BaseNode block : blocks) {
            lastVal = block.eval(blockScope, context);

            // 检查控制流标志，如果设置了则提前退出
            if (context.isReturnFlag() || context.isBreakFlag() || context.isContinueFlag()) {
                break;
            }
        }

        // 清理当前self值
        context.setCurrentSelfValue(null);

        // 返回最后一个表达式的值
        return lastVal;
    }
}
