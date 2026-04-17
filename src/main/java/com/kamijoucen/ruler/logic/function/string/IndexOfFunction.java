package com.kamijoucen.ruler.logic.function.string;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class IndexOfFunction implements RulerFunction {

    @Override
    public String getName() {
        return "stringIndexOf";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        int off = FunctionParamUtil.offset(self);
        if (str == null || param == null || param.length < off + 1 || !(param[off] instanceof StringValue)) {
            throw new RulerRuntimeException("stringIndexOf expects strings");
        }
        StringValue sub = (StringValue) param[off];
        int fromIndex = 0;
        if (param.length >= off + 2 && param[off + 1] instanceof IntegerValue) {
            fromIndex = (int) ((IntegerValue) param[off + 1]).getValue();
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(str.getValue().indexOf(sub.getValue(), fromIndex));
    }
}
