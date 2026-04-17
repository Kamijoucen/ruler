package com.kamijoucen.ruler.logic.function.object;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.HashMap;
import java.util.Map;

public class MergeFunction implements RulerFunction {

    @Override
    public String getName() {
        return "merge";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            return null;
        }
        if (!(param[0] instanceof RsonValue) || !(param[1] instanceof RsonValue)) {
            throw new RulerRuntimeException("merge expects objects");
        }
        RsonValue obj1 = (RsonValue) param[0];
        RsonValue obj2 = (RsonValue) param[1];
        Map<String, BaseValue> result = new HashMap<>(obj1.getFields());
        result.putAll(obj2.getFields());
        return new RsonValue(result);
    }
}
