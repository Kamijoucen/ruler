package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.ValueConvert;

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

        Object[] realParam = processParams(param);

        Object returnVal = function.call(realParam);

        ValueConvert converter = ConvertRepository.getConverter(returnVal);

        if (converter == null) {
            return ConvertRepository.getConverter(ValueType.STRING).realToBase(returnVal.toString());
        }

        return converter.realToBase(returnVal);
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
