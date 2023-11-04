package com.kamijoucen.ruler.value;

import java.util.Map;

public class ProxyValue extends RsonValue {

    private BaseValue value;

    private RsonValue configValue;

    public ProxyValue(BaseValue value, RsonValue configValue) {
        super(null);
        this.value = value;
        this.configValue = configValue;
    }

    @Override
    public Map<String, BaseValue> getFields() {
        throw new UnsupportedOperationException("proxy value can not get fields");
    }

    public BaseValue getValue() {
        return value;
    }

    public RsonValue getConfigValue() {
        return configValue;
    }

}
