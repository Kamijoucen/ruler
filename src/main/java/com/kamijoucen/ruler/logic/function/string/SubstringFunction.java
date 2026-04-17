package com.kamijoucen.ruler.logic.function.string;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class SubstringFunction implements RulerFunction {

    @Override
    public String getName() {
        return "stringSubstring";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        if (str == null) {
            throw new RulerRuntimeException("stringSubstring expects a string");
        }
        int off = FunctionParamUtil.offset(self);
        if (param == null || param.length < off + 1) {
            return null;
        }
        if (!(param[off] instanceof IntegerValue)) {
            throw new RulerRuntimeException("stringSubstring expects an integer start index");
        }
        int start = (int) ((IntegerValue) param[off]).getValue();
        if (param.length >= off + 2 && param[off + 1] instanceof IntegerValue) {
            int end = (int) ((IntegerValue) param[off + 1]).getValue();
            return new StringValue(str.getValue().substring(start, end));
        }
        return new StringValue(str.getValue().substring(start));
    }
}
