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
import java.util.ArrayList;
import java.util.List;

public class MapFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayMap";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayMap expects an array");
        }
        if (param == null || param.length < off + 1) {
            return null;
        }
        BaseValue callback = (BaseValue) param[off];
        List<BaseValue> result = new ArrayList<>();
        int index = 0;
        for (BaseValue item : arr.getValues()) {
            BaseValue mapped = CallLogic.callValue(callback, currentScope, context, item,
                    IntegerValue.valueOf(BigInteger.valueOf(index)), arr);
            result.add(mapped);
            index++;
        }
        return new ArrayValue(result);
    }

}
