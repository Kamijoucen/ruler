package com.kamijoucen.ruler.function.classinfo;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.convert.ValueConvert;

public class PrintSelfFunction implements RulerFunction {
    @Override
    public String getName() {
        return "println";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ValueConvert convert = context.getConfiguration().getValueConvertManager().getConverter(self.getType());
        Object realValue = convert.baseToReal(self, context.getConfiguration());
        System.out.println(realValue);
        return NullValue.INSTANCE;
    }
}
