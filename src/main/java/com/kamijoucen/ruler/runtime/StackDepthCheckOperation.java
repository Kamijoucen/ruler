package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 栈深度检查操作
 * 用于防止栈溢出
 *
 * @author Kamijoucen
 */
public class StackDepthCheckOperation {

    private int currentDepth = 0;

    /**
     * 进入新的调用帧时调用
     *
     * @param context 运行时上下文
     * @param location 调用位置
     * @throws RulerRuntimeException 如果超出最大栈深度
     */
    public void pushCallStack(RuntimeContext context, TokenLocation location) {
        currentDepth++;
        int maxDepth = context.getConfiguration().getMaxStackDepth();
        // -1 表示不限制栈深度
        if (maxDepth > 0 && currentDepth > maxDepth) {
            throw RulerRuntimeException.stackOverflow(maxDepth, location);
        }
    }

    /**
     * 退出调用帧时调用
     */
    public void popCallStack() {
        currentDepth--;
    }

    /**
     * 获取当前栈深度
     *
     * @return 当前栈深度
     */
    public int getCurrentDepth() {
        return currentDepth;
    }
}
