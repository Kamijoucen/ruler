package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.PanicException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class PanicFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Panic";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        String message = null;
        if (param.length != 0) {
            message = param[0].toString();
        }
        throw new PanicException(message);
    }
}
