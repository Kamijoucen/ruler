package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

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
