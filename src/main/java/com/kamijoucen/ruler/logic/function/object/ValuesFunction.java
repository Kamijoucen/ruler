package com.kamijoucen.ruler.logic.function.object;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.ArrayList;
import java.util.List;

public class ValuesFunction implements RulerFunction {

    @Override
    public String getName() {
        return "values";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        if (!(param[0] instanceof RsonValue)) {
            throw new RulerRuntimeException("values expects an object");
        }
        RsonValue obj = (RsonValue) param[0];
        List<BaseValue> values = new ArrayList<>(obj.getFields().values());
        return new ArrayValue(values);
    }
}
