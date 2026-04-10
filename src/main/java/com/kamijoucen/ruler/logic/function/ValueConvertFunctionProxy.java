package com.kamijoucen.ruler.logic.function;


import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

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
    public BaseValue call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        Object[] realParam = convertParams(param);
        Object returnVal = function.call(context, currentScope, self, realParam);

        ValueConvert convert = context.getConfiguration().getValueConvertManager().getConverter(returnVal);
        if (convert == null) {
            return context.getConfiguration().getValueConvertManager().getConverter(ValueType.STRING)
                    .realToBase(returnVal.toString(), configuration);
        }
        return convert.realToBase(returnVal, configuration);
    }

    private Object[] convertParams(Object... param) {
        Object[] realValues = new Object[param.length];
        for (int i = 0; i < param.length; i++) {
            realValues[i] = convert(param[i]);
        }
        return realValues;
    }

    private Object convert(Object param) {
        if (!(param instanceof BaseValue)) {
            throw new RulerRuntimeException("invalid argument type");
        }
        BaseValue baseValue = (BaseValue) param;
        ValueConvert convert = configuration.getValueConvertManager().getConverter(baseValue.getType());
        return convert.baseToReal(baseValue, configuration);
    }

}
