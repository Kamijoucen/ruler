package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class CallFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Call";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        final int len = param.length;
        if (len == 0) {
            return NullValue.INSTANCE;
        }
        if (!(param[0] instanceof StringValue)) {
            return NullValue.INSTANCE;
        }
        final BaseValue callValue = currentScope.find(((StringValue) param[0]).getValue());
        if (callValue == null) {
            return NullValue.INSTANCE;
        }
        return callValue;
    }

}
