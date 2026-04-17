package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class PopFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayPop";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        if (arr == null) {
            throw new RulerRuntimeException("arrayPop expects an array");
        }
        if (arr.getValues().isEmpty()) {
            return NullValue.INSTANCE;
        }
        return arr.getValues().remove(arr.getValues().size() - 1);
    }
}
