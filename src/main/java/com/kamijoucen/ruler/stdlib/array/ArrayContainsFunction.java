package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

public class ArrayContainsFunction implements RulerFunction {

    @Override
    public String getName() {
        return "contains";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            return BoolValue.get(false);
        }
        if (param == null || param.length < off + 1) {
            return BoolValue.get(false);
        }
        BaseValue target = (BaseValue) param[off];
        for (BaseValue value : arr.getValues()) {
            if (value.equals(target)) {
                return BoolValue.get(true);
            }
        }
        return BoolValue.get(false);
    }
}
