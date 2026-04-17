package com.kamijoucen.ruler.logic.function.string;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.ArrayList;
import java.util.List;

public class SplitFunction implements RulerFunction {

    @Override
    public String getName() {
        return "stringSplit";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        int off = FunctionParamUtil.offset(self);
        if (str == null || param == null || param.length < off + 1 || !(param[off] instanceof StringValue)) {
            throw new RulerRuntimeException("stringSplit expects strings");
        }
        StringValue regex = (StringValue) param[off];
        String[] parts = str.getValue().split(regex.getValue());
        List<BaseValue> values = new ArrayList<>();
        for (String part : parts) {
            values.add(new StringValue(part));
        }
        return new ArrayValue(values);
    }
}
