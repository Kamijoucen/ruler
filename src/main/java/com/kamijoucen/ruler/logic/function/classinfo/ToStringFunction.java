package com.kamijoucen.ruler.logic.function.classinfo;

import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class ToStringFunction implements RulerFunction {
    @Override
    public String getName() {
        return "ToString";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        return new StringValue(self.toString());
    }
}
