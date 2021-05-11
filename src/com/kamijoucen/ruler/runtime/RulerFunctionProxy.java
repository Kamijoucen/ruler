package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.value.BaseValue;

public class RulerFunctionProxy implements RulerFunction {

    private RulerFunction function;

    public RulerFunctionProxy(RulerFunction function) {
        this.function = function;
    }

    @Override
    public String getName() {
        return function.getName();
    }

    @Override
    public BaseValue call(Object... param) {
        return null;
    }


}
