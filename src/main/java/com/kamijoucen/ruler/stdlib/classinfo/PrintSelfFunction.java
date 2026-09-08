package com.kamijoucen.ruler.stdlib.classinfo;

import com.kamijoucen.ruler.logic.convert.ValueConversions;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueConvert;

public class PrintSelfFunction implements RulerFunction {
    @Override
    public String getName() {
        return "println";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ValueConvert convert = ValueConversions.getConverter(self.getType());
        Object realValue = convert.baseToReal(self);
        System.out.println(realValue);
        return NullValue.INSTANCE;
    }
}
