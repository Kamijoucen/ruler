package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class ArrayIsEmptyFunction implements RulerFunction {

    @Override
    public String getName() {
        return "isEmpty";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        if (arr == null) {
            return BoolValue.get(true);
        }
        return BoolValue.get(arr.getValues().isEmpty());
    }
}
