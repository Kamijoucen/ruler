package com.kamijoucen.ruler.logic.convert;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.value.*;

/** Java/Ruler value conversion at the host function boundary. */
public final class HostCalls {
    private HostCalls() {}

    public static BaseValue call(RulerFunction function, RuntimeContext context, Scope scope,
                                 BaseValue self, Object... args) {
        Object[] realArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof BaseValue)) {
                throw new RulerRuntimeException("invalid argument type");
            }
            BaseValue value = (BaseValue) args[i];
            realArgs[i] = ValueConversions.getConverter(value.getType()).baseToReal(value);
        }
        Object result = function.call(context, scope, self, realArgs);
        ValueConvert converter = ValueConversions.getConverter(result);
        if (converter == null) {
            return ValueConversions.getConverter(ValueType.STRING).realToBase(result.toString());
        }
        return converter.realToBase(result);
    }
}
