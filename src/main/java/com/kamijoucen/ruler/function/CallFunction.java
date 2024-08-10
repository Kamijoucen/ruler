package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.StringValue;

public class CallFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Call";
    }

    @Override
    public Object call(RuntimeContext context, Environment env, BaseValue self, Object... param) {
        final int len = param.length;
        if (len == 0) {
            return NullValue.INSTANCE;
        }
        if (!(param[0] instanceof StringValue)) {
            return NullValue.INSTANCE;
        }
        final BaseValue callValue = env.find(((StringValue) param[0]).getValue());
        if (callValue == null) {
            return NullValue.INSTANCE;
        }
        return callValue;
    }

}
