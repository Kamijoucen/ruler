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

public class ObjectPickFunction implements RulerFunction {

    @Override
    public String getName() {
        return "pick";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (!(self instanceof RsonValue)) {
            throw new RulerRuntimeException("pick() must be called on an object");
        }
        RsonValue obj = (RsonValue) self;
        if (param == null || param.length == 0 || !(param[0] instanceof ArrayValue)) {
            throw new RulerRuntimeException("pick() expects an array of keys");
        }
        ArrayValue keysArr = (ArrayValue) param[0];
        Map<String, BaseValue> result = new HashMap<>();
        for (BaseValue keyValue : keysArr.getValues()) {
            if (keyValue instanceof StringValue) {
                String key = ((StringValue) keyValue).getValue();
                BaseValue value = obj.getFields().get(key);
                if (value != null) {
                    result.put(key, value);
                }
            }
        }
        return new RsonValue(result);
    }
}
