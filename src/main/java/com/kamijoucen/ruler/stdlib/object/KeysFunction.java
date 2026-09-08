package com.kamijoucen.ruler.stdlib.object;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.RsonValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.util.ArrayList;
import java.util.List;

public class KeysFunction implements RulerFunction {

    @Override
    public String getName() {
        return "keys";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        if (!(param[0] instanceof RsonValue)) {
            throw new RulerRuntimeException("keys expects an object");
        }
        RsonValue obj = (RsonValue) param[0];
        List<BaseValue> keys = new ArrayList<>();
        for (String key : obj.getFields().keySet()) {
            keys.add(new StringValue(key));
        }
        return new ArrayValue(keys);
    }
}
