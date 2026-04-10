package com.kamijoucen.ruler.logic.function.classinfo;

import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

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
