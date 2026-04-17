package com.kamijoucen.ruler.logic.function.string;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.logic.util.NumberUtil;

import java.math.BigInteger;

public class StringIndexOfFunction implements RulerFunction {

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
            fromIndex = NumberUtil.toIntIndex((IntegerValue) param[off + 1]);
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(BigInteger.valueOf(str.getValue().indexOf(sub.getValue(), fromIndex)));
    }
}
