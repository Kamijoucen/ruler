package com.kamijoucen.ruler.stdlib.object;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.RsonValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.types.spi.RulerFunction;

public class HasKeyFunction implements RulerFunction {

    @Override
    public String getName() {
        return "hasKey";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            return BoolValue.get(false);
        }
        if (!(param[0] instanceof RsonValue) || !(param[1] instanceof StringValue)) {
            throw new RulerRuntimeException("hasKey expects an object and a string key");
        }
        RsonValue obj = (RsonValue) param[0];
        String key = ((StringValue) param[1]).getValue();
        return BoolValue.get(obj.getFields().containsKey(key));
    }
}
