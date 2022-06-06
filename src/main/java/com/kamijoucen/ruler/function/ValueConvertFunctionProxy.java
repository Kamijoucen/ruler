package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.ValueConvert;

public class ValueConvertFunctionProxy implements RulerFunction {

    private final RulerFunction function;
    private final RulerConfiguration configuration;

    public ValueConvertFunctionProxy(RulerFunction function, RulerConfiguration configuration) {
        this.function = function;
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return function.getName();
    }

    @Override
    public BaseValue call(Object... param) {
        Object[] realParam = convertParams(param);
        Object returnVal = function.call(realParam);
        ValueConvert converter = ConvertRepository.getConverter(returnVal);
        if (converter == null) {
            return ConvertRepository.getConverter(ValueType.STRING).realToBase(returnVal.toString(), configuration);
        }
        return converter.realToBase(returnVal, configuration);
    }

    private Object[] convertParams(Object... param) {
        Object[] realValues = new Object[param.length];
        for (int i = 0; i < param.length; i++) {
            realValues[i] = convert(param[i]);
        }
        return realValues;
    }

    private Object convert(Object param) {
        BaseValue baseValue = null;
        if (param instanceof BaseValue) {
            baseValue = (BaseValue) param;
        } else {
            throw SyntaxException.withSyntax("错误的类型");
        }
        return ConvertRepository.getConverter(baseValue.getType()).baseToReal(baseValue, configuration);
    }

}
