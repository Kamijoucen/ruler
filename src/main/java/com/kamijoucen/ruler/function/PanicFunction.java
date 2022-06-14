package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.PanicException;

public class PanicFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Panic";
    }

    @Override
    public Object call(Object... param) {
        String message = null;
        if (param.length != 0) {
            message = param[0].toString();
        }
        throw new PanicException(message);
    }
}
