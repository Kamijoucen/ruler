package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class FuncParamLengthTestFunction implements RulerFunction {

    @Override
    public String getName() {
        return "FuncParamLengthTestFunction";
    }

    @Override
    public Object call(RuntimeContext context, Environment env, BaseValue self, Object... param) {
        return param.length;
    }

}
