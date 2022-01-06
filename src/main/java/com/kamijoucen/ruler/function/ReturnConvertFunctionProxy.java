package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.common.ConvertRepository;

public class ReturnConvertFunctionProxy implements RulerFunction {

    private final RulerFunction function;

    public ReturnConvertFunctionProxy(RulerFunction function) {
        this.function = function;
    }

    @Override
    public String getName() {
        return function.getName();
    }

    @Override
    public Object call(Object... param) {
        Object value = function.call(param);
        return ConvertRepository.getConverter(value).realToBase(value);
    }

}
