package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.math.BigInteger;

public class UnshiftFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayUnshift";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayUnshift expects an array");
        }
        if (param == null || param.length < off + 1) {
            return context.getConfiguration().getIntegerNumberCache().getValue(BigInteger.valueOf(arr.getValues().size()));
        }
        for (int i = param.length - 1; i >= off; i--) {
            arr.getValues().add(0, (BaseValue) param[i]);
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(BigInteger.valueOf(arr.getValues().size()));
    }
}
