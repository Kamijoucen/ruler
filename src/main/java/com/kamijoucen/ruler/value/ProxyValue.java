package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.exception.TypeException;

import java.util.Map;

/**
 * 代理值
 * 用于包装其他类型的值，并提供配置信息
 *
 * @author Kamijoucen
 */
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
        throw TypeException.unsupportedOperation("获取字段", this.getType(), null);
    }

    public BaseValue getValue() {
        return value;
    }

    public RsonValue getConfigValue() {
        return configValue;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
