package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ProxyValue;
import com.kamijoucen.ruler.types.value.RsonValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class ProxyFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Proxy";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            throw new IllegalArgumentException("Proxy args is null");
        }
        BaseValue value = (BaseValue) param[0];
        if (param.length == 1) {
            return value;
        }
        BaseValue configValue = (BaseValue) param[1];
        if (configValue == null || configValue.getType() == ValueType.NULL) {
            return value;
        }
        if (configValue.getType() != ValueType.RSON) {
            throw new IllegalArgumentException("proxy config must be rson");
        }
        return new ProxyValue(value, (RsonValue) configValue);
    }

}
