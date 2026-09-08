package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.IntegerValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.logic.util.NumberUtil;

public class CharAtFunction implements RulerFunction {

    @Override
    public String getName() {
        return "StringCharAt";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        int off = FunctionParamUtil.offset(self);
        if (str == null) {
            throw new RulerRuntimeException("StringCharAt expects a string");
        }
        if (param == null || param.length < off + 1 || !(param[off] instanceof IntegerValue)) {
            throw new RulerRuntimeException("StringCharAt expects an integer");
        }
        IntegerValue index = (IntegerValue) param[off];
        return new StringValue(String.valueOf(str.getValue().charAt(NumberUtil.toIntIndex(index))));
    }
}
