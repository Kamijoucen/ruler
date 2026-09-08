package com.kamijoucen.ruler.stdlib.string;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.IntegerValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;
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
        return IntegerValue.valueOf(BigInteger.valueOf(str.getValue().indexOf(sub.getValue(), fromIndex)));
    }
}
