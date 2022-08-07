package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;

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
    public Object call(RuntimeContext context, Object... param) {
        Object value = function.call(context, param);
        return ConvertRepository.getConverter(value).realToBase(value, configuration);
    }

}
