package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.convert.ValueConvert;

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
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        Object value = function.call(context, currentScope, self, param);
        ValueConvert convert = context.getConfiguration().getValueConvertManager().getConverter(value);
        return convert.realToBase(value, configuration);
    }

}
