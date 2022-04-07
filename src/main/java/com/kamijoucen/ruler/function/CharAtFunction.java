package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.StringValue;

public class CharAtFunction implements RulerFunction {

    @Override
    public String getName() {
        return "stringCharAt";
    }

    @Override
    public Object call(Object... param) {
        if (!(param[0] instanceof StringValue)) {
            throw new RuntimeException("stringCharAt function only accept string type");
        }
        if (!(param[1] instanceof IntegerValue)) {
            throw new RuntimeException("stringCharAt function only accept integer type");
        }
        StringValue value = (StringValue) param[0];
        IntegerValue index = (IntegerValue) param[1];
        return value.getValue().charAt(index.getValue());
    }
}
