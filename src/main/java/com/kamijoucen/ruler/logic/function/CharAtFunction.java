package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.StringValue;
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
