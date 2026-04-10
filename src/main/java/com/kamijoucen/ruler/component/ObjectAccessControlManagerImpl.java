package com.kamijoucen.ruler.component;

import java.util.Objects;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import com.kamijoucen.ruler.domain.value.ModuleValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.ProxyValue;
import com.kamijoucen.ruler.domain.value.RClass;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class ObjectAccessControlManagerImpl implements ObjectAccessControlManager {

    @Override
    public BaseValue accessObject(BaseValue value, String name, RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxyValue = (ProxyValue) value;

            BaseValue getCallback = this.accessObject(proxyValue.getConfigValue(), "get", context);
            if (getCallback.getType() == ValueType.CLOSURE) {
                CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
                return executor.call(((ClosureValue) getCallback), null,
                        context, proxyValue.getValue(), new StringValue(name));
            }
        }
        BaseValue result = null;
        if (value instanceof ModuleValue) {
            result = ((ModuleValue) value).getModuleScope().find(name);
        } else if (value.getType() == ValueType.RSON) {
            result = ((RsonValue) value).getFields().get(name);
        }
        if (result == null) {
            RClass rClass =
                    context.getConfiguration().getRClassManager().getClassValue(value.getType());
            Objects.requireNonNull(rClass, "class not found: " + value.getType());
            result = rClass.getProperty(name);
        }
        return result == null ? NullValue.INSTANCE : result;
    }

    @Override
    public void modifyObject(BaseValue value, String name, BaseValue newValue,
            RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxyValue = (ProxyValue) value;
            BaseValue putCallback = this.accessObject(proxyValue.getConfigValue(), "put", context);
            if (putCallback != null && putCallback.getType() == ValueType.CLOSURE) {
                CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
                executor.call(((ClosureValue) putCallback), null, context,
                        proxyValue.getValue(), new StringValue(name), newValue);
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
