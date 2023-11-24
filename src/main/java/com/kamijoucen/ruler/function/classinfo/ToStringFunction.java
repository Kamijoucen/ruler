package com.kamijoucen.ruler.function.classinfo;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ToStringFunction implements RulerFunction {
    @Override
    public String getName() {
        return "ToString";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        return self.toString();
    }
}
