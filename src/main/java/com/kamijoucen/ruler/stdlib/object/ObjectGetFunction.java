package com.kamijoucen.ruler.stdlib.object;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.RsonValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.types.spi.RulerFunction;

public class ObjectGetFunction implements RulerFunction {

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (!(self instanceof RsonValue)) {
            throw new RulerRuntimeException("get() must be called on an object");
        }
        RsonValue obj = (RsonValue) self;
        if (param == null || param.length == 0 || !(param[0] instanceof StringValue)) {
            throw new RulerRuntimeException("get() expects a string key");
        }
        String key = ((StringValue) param[0]).getValue();
        BaseValue value = obj.getFields().get(key);
        if (value != null) {
            return value;
        }
        if (param.length >= 2) {
            return (BaseValue) param[1];
        }
        return NullValue.INSTANCE;
    }
}
