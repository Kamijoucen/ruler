package com.kamijoucen.ruler.stdlib.string;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

public class ReplaceFunction implements RulerFunction {

    @Override
    public String getName() {
        return "stringReplace";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        int off = FunctionParamUtil.offset(self);
        if (str == null || param == null || param.length < off + 2 || !(param[off] instanceof StringValue) || !(param[off + 1] instanceof StringValue)) {
            throw new RulerRuntimeException("stringReplace expects strings");
        }
        StringValue target = (StringValue) param[off];
        StringValue replacement = (StringValue) param[off + 1];
        return new StringValue(str.getValue().replace(target.getValue(), replacement.getValue()));
    }
}
