package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.StringValue;

public class CharAtFunction implements RulerFunction {

    @Override
    public String getName() {
        return "StringCharAt";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        if (!(param[0] instanceof StringValue)) {
            throw new RuntimeException("stringCharAt function only accept string type");
        }
        if (!(param[1] instanceof IntegerValue)) {
            throw new RuntimeException("stringCharAt function only accept integer type");
        }
        StringValue value = (StringValue) param[0];
        IntegerValue index = (IntegerValue) param[1];
        return value.getValue().charAt((int) index.getValue());
    }
}
