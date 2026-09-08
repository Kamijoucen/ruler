package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.exception.PanicException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class PanicFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Panic";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        String message = null;
        if (param.length != 0) {
            message = param[0].toString();
        }
        throw new PanicException(message);
    }
}
