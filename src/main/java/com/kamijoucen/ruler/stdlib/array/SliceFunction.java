package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.IntegerValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.logic.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class SliceFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arraySlice";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arraySlice expects an array");
        }
        if (param == null || param.length < off + 1 || !(param[off] instanceof IntegerValue)) {
            throw new RulerRuntimeException("arraySlice expects an array and integer start index");
        }
        int start = NumberUtil.toIntIndex((IntegerValue) param[off]);
        int end = arr.getValues().size();
        if (param.length >= off + 2 && param[off + 1] instanceof IntegerValue) {
            end = NumberUtil.toIntIndex((IntegerValue) param[off + 1]);
        }
        List<BaseValue> values = arr.getValues();
        int actualStart = Math.max(0, start);
        int actualEnd = Math.min(values.size(), end);
        List<BaseValue> result = new ArrayList<>();
        for (int i = actualStart; i < actualEnd; i++) {
            result.add(values.get(i));
        }
        return new ArrayValue(result);
    }
}
