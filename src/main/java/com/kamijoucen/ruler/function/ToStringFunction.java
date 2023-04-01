package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class ToStringFunction implements RulerFunction {
    @Override
    public String getName() {
        return "ToString";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        if (self.getType() == ValueType.RSON) {
            // todo print json ?
        }
        return self.toString();
    }
}
