package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class ArrayLastFunction implements RulerFunction {

    @Override
    public String getName() {
        return "last";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        if (arr == null || arr.getValues().isEmpty()) {
            throw new RulerRuntimeException("last() called on empty array");
        }
        return arr.getValues().get(arr.getValues().size() - 1);
    }
}
