package com.kamijoucen.ruler.function.classinfo;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class ToStringFunction implements RulerFunction {
    @Override
    public String getName() {
        return "ToString";
    }

    @Override
    public Object call(RuntimeContext context, Environment env, BaseValue self, Object... param) {
        return new StringValue(self.toString());
    }
}
