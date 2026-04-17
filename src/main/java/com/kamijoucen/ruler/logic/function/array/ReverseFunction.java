package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

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
