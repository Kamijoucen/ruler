package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class LengthFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Length";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        if (self.getType() == ValueType.ARRAY) {
            return context.getConfiguration().getIntegerNumberCache().getValue(((ArrayValue) self).getValues().size());
        }
        return null;
    }

}
