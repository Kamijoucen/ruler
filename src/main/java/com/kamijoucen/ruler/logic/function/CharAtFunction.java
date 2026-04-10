package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class CharAtFunction implements RulerFunction {

    @Override
    public String getName() {
        return "StringCharAt";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (!(param[0] instanceof StringValue)) {
            throw new RulerRuntimeException("StringCharAt expects a string");
        }
        if (!(param[1] instanceof IntegerValue)) {
            throw new RulerRuntimeException("StringCharAt expects an integer");
        }
        StringValue value = (StringValue) param[0];
        IntegerValue index = (IntegerValue) param[1];
        return value.getValue().charAt((int) index.getValue());
    }
}
