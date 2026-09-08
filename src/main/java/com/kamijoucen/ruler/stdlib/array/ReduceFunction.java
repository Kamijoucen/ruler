package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.*;
import com.kamijoucen.ruler.logic.eval.CallLogic;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.math.BigInteger;

public class ReduceFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayReduce";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayReduce expects an array");
        }
        if (param == null || param.length < off + 1) {
            return null;
        }
        BaseValue callback = (BaseValue) param[off];
        boolean hasInitial = param.length >= off + 2;
        BaseValue accumulator = hasInitial ? (BaseValue) param[off + 1] : null;
        int startIndex = 0;

        if (!hasInitial) {
            if (arr.getValues().isEmpty()) {
                throw new RulerRuntimeException("arrayReduce of empty array with no initial value");
            }
            accumulator = arr.getValues().get(0);
            startIndex = 1;
        }

        for (int i = startIndex; i < arr.getValues().size(); i++) {
            BaseValue item = arr.getValues().get(i);
            accumulator = CallLogic.callValue(callback, currentScope, context, accumulator, item,
                    IntegerValue.valueOf(BigInteger.valueOf(i)), arr);
        }
        return accumulator;
    }

}
