package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public interface RulerFunction {

    /**
     * 函数名
     *
     * @return 函数名
     */
    String getName();


    /**
     * 函数实现
     *
     * @param context 运行时上下文
     * @param currentScope 当前作用域
     * @param self 调用者
     * @param param 参数
     * @return 函数返回值
     */
    Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param);

}
