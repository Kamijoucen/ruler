package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class ToStringFunction implements RulerFunction {
    @Override
    public String getName() {
        return "ToString";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        return self.toString();
    }
}
