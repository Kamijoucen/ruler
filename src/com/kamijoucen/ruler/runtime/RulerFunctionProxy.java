package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;

public class RulerFunctionProxy implements RulerFunction {

    private final RulerFunction function;

    public RulerFunctionProxy(RulerFunction function) {
        this.function = function;
    }

    @Override
    public String getName() {
        return function.getName();
    }

    @Override
    public BaseValue call(Object... param) {

        Object[] readParam = processParams(param);

        Object returnVal = function.call(readParam);

        return null;
    }

    private Object[] processParams(Object... param) {

        Object[] realValues = new Object[param.length];

        for (int i = 0; i < param.length; i++) {
            realValues[i] = processParam(param[i]);
        }

        return realValues;
    }

    private Object processParam(Object param) {

        BaseValue baseValue = null;

        if (param instanceof BaseValue) {
            baseValue = (BaseValue) param;
        } else {
            throw SyntaxException.withSyntax("错误的类型");
        }

        return ConvertRepository.getConverter(baseValue.getType()).baseToReal(baseValue);
    }

}
