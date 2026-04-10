package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class TimestampFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Timestamp";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        return System.currentTimeMillis();
    }
}
