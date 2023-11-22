package com.kamijoucen.ruler.config.impl;

import java.util.Objects;

import com.kamijoucen.ruler.config.ObjectAccessControlManager;
import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.ModuleValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.ProxyValue;
import com.kamijoucen.ruler.value.RClass;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

public class ObjectAccessControlManagerImpl implements ObjectAccessControlManager {

    @Override
    public BaseValue accessObject(BaseValue value, String name, RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxyValue = (ProxyValue) value;

            BaseValue getCallback = this.accessObject(proxyValue.getConfigValue(), "get", context);
            if (getCallback.getType() == ValueType.CLOSURE) {
                CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
                return executor.call(proxyValue.getValue(), ((ClosureValue) getCallback), null, context,
                        proxyValue.getValue(), new StringValue(name));
            }
        }
        BaseValue result = null;
        if (value instanceof ModuleValue) {
            result = ((ModuleValue) value).getModuleScope().find(name);
        } else if (value.getType() == ValueType.RSON) {
            result = ((RsonValue) value).getFields().get(name);
        }
        if (result == null) {
            RClass rClass = context.getConfiguration().getRClassManager().getClassValue(value.getType());
            Objects.requireNonNull(rClass, "class not found: " + value.getType());
            result = rClass.getProperty(name);
        }
        return result == null ? NullValue.INSTANCE : result;
    }

    @Override
    public void modifyObject(BaseValue value, String name, BaseValue newValue, RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxyValue = (ProxyValue) value;
            BaseValue putCallback = this.accessObject(proxyValue.getConfigValue(), "put", context);
            if (putCallback != null && putCallback.getType() == ValueType.CLOSURE) {
                CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
                executor.call(proxyValue.getValue(), ((ClosureValue) putCallback), null, context, proxyValue.getValue(),
                        new StringValue(name),
                        newValue);
                return;
            } else {
                this.modifyObject(proxyValue.getValue(), name, newValue, context);
                return;
            }
        }
        if (value.getType() != ValueType.RSON) {
            // 不允许修改对象的元信息
            throw new UnsupportedOperationException("can not modify object: " + value.getType());
        }
        ((RsonValue) value).getFields().put(name, newValue);
    }

}
