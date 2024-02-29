package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class FuncParamLengthTestFunction implements RulerFunction {

    @Override
    public String getName() {
        return "FuncParamLengthTestFunction";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self,
            Object... param) {
        return param.length;
    }

}
