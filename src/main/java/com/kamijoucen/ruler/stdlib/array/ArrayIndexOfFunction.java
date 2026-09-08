package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.math.BigInteger;

public class ArrayIndexOfFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayIndexOf";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayIndexOf expects an array");
        }
        if (param == null || param.length < off + 1) {
            return IntegerValue.valueOf(BigInteger.valueOf(-1));
        }
        BaseValue target = (BaseValue) param[off];
        for (int i = 0; i < arr.getValues().size(); i++) {
            if (arr.getValues().get(i).equals(target)) {
                return IntegerValue.valueOf(BigInteger.valueOf(i));
            }
        }
        return IntegerValue.valueOf(BigInteger.valueOf(-1));
    }
}
