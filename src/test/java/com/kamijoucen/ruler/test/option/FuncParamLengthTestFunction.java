package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

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
