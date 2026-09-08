package com.kamijoucen.ruler.stdlib.classinfo;

import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.StringValue;

public class ToStringFunction implements RulerFunction {
    @Override
    public String getName() {
        return "toString";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        return new StringValue(self.toString());
    }
}
