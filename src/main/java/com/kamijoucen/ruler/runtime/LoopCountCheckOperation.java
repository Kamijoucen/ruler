package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.RulerRuntimeException;

/**
 * 循环计数检查操作
 * 用于防止无限循环
 *
 * @author Kamijoucen
 */
public class LoopCountCheckOperation {

    private int count = 0;

    /**
     * 执行循环计数检查
     *
     * @param node 循环节点
     * @param scope 作用域
     * @param context 运行时上下文
     * @throws RulerRuntimeException 如果超出最大循环次数
     */
    public void accept(BaseNode node, Scope scope, RuntimeContext context) {
        int maxLoopNumber = context.getConfiguration().getMaxLoopNumber();
        if (maxLoopNumber <= 0) {
            return; // 不限制循环次数
        }

        count++;
        if (count > maxLoopNumber) {
            throw RulerRuntimeException.loopLimitExceeded(maxLoopNumber, node.getLocation());
        }
    }

    /**
     * 重置计数器
     */
    public void reset() {
        count = 0;
    }

    /**
     * 获取当前循环次数
     *
     * @return 当前循环次数
     */
    public int getCount() {
        return count;
    }
}
