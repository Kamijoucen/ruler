package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
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
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            throw new IllegalArgumentException("the args of Proxy function is null");
        }
        BaseValue value = (BaseValue) param[0];
        // 如果已经是代理对象了，直接返回
        if (value instanceof ProxyValue) {
            return value;
        }
        if (param.length == 1) {
            return value;
        }
        BaseValue configValue = (BaseValue) param[1];
        if (configValue == null || configValue.getType() == ValueType.NULL) {
            return value;
        }
        if (configValue.getType() != ValueType.RSON) {
            throw new IllegalArgumentException("the proxy config info not is rson!");
        }
        return new ProxyValue(value, (RsonValue) configValue);
    }

}
