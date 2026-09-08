package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.util.Collections;

public class ReverseFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayReverse";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        if (arr == null) {
            throw new RulerRuntimeException("arrayReverse expects an array");
        }
        Collections.reverse(arr.getValues());
        return arr;
    }
}
