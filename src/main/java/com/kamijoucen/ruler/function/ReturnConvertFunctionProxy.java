package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.config.RulerConfiguration;

public class ReturnConvertFunctionProxy implements RulerFunction {

    private final RulerFunction function;
    private final RulerConfiguration configuration;

    public ReturnConvertFunctionProxy(RulerFunction function, RulerConfiguration configuration) {
        this.function = function;
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return function.getName();
    }

    @Override
    public Object call(Object... param) {
        Object value = function.call(param);
        return ConvertRepository.getConverter(value).realToBase(value, configuration);
    }

}
