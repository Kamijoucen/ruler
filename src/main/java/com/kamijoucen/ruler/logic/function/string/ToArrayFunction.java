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

public class ToArrayFunction implements RulerFunction {

    @Override
    public String getName() {
        return "StringToArray";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        StringValue str = FunctionParamUtil.string(self, param);
        if (str == null) {
            throw new RulerRuntimeException("StringToArray expects a string");
        }
        String s = str.getValue();
        List<BaseValue> chars = new ArrayList<>(s.length());
        for (int i = 0; i < s.length(); i++) {
            chars.add(new StringValue(String.valueOf(s.charAt(i))));
        }
        return new ArrayValue(chars);
    }
}
