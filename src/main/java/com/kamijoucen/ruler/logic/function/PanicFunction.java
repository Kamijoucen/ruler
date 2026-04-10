package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.exception.PanicException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
