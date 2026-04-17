package com.kamijoucen.ruler.logic.function.string;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class UpperCaseFunction implements RulerFunction {

    @Override
    public String getName() {
        return "stringUpperCase";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        if (str == null) {
            throw new RulerRuntimeException("stringUpperCase expects a string");
        }
        return new StringValue(str.getValue().toUpperCase());
    }
}
