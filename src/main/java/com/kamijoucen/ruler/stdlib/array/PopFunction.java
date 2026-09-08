package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

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
