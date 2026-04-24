package com.kamijoucen.ruler.logic.function.object;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.HashMap;
import java.util.Map;

public class ObjectOmitFunction implements RulerFunction {

    @Override
    public String getName() {
        return "omit";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (!(self instanceof RsonValue)) {
            throw new RulerRuntimeException("omit() must be called on an object");
        }
        RsonValue obj = (RsonValue) self;
        if (param == null || param.length == 0 || !(param[0] instanceof ArrayValue)) {
            throw new RulerRuntimeException("omit() expects an array of keys");
        }
        ArrayValue keysArr = (ArrayValue) param[0];
        Map<String, BaseValue> result = new HashMap<>(obj.getFields());
        for (BaseValue keyValue : keysArr.getValues()) {
            if (keyValue instanceof StringValue) {
                String key = ((StringValue) keyValue).getValue();
                result.remove(key);
            }
        }
        return new RsonValue(result);
    }
}
