package com.kamijoucen.ruler.logic.function.object;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

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
