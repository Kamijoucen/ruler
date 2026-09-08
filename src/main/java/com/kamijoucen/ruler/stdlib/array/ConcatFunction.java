package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.util.ArrayList;
import java.util.List;

public class ConcatFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayConcat";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr1 = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr1 == null) {
            throw new RulerRuntimeException("arrayConcat expects arrays");
        }
        List<BaseValue> result = new ArrayList<>(arr1.getValues());
        for (int i = off; i < param.length; i++) {
            if (!(param[i] instanceof ArrayValue)) {
                throw new RulerRuntimeException("arrayConcat expects arrays");
            }
            result.addAll(((ArrayValue) param[i]).getValues());
        }
        return new ArrayValue(result);
    }
}
