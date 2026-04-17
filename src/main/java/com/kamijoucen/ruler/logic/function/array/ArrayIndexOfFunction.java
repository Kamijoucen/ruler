package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

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
            return context.getConfiguration().getIntegerNumberCache().getValue(-1);
        }
        BaseValue target = (BaseValue) param[off];
        for (int i = 0; i < arr.getValues().size(); i++) {
            if (arr.getValues().get(i).equals(target)) {
                return context.getConfiguration().getIntegerNumberCache().getValue(i);
            }
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(-1);
    }
}
