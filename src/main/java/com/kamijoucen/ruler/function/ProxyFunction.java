package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ProxyValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.ValueType;

public class ProxyFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Proxy";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {

        if (param == null || param.length == 0) {
            throw new IllegalArgumentException("the args of Proxy fucntion is null");
        }
        BaseValue value = (BaseValue) param[0];

        if (value.getType() != ValueType.RSON) {
            throw new IllegalArgumentException("the value not is rson!");
        }

        if (param.length == 1) {
            return new ProxyValue((RsonValue) value, null);
        }

        BaseValue configValue = (BaseValue) param[1];
        if (configValue.getType() != ValueType.RSON) {
            throw new IllegalArgumentException("the callback info not is rson!");
        }
        return new ProxyValue((RsonValue) value, (RsonValue) configValue);

    }

}
