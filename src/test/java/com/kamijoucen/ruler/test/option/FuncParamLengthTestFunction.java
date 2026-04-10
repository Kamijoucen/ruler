package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
