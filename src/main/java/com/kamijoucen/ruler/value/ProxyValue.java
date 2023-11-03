package com.kamijoucen.ruler.value;

import java.util.Map;

import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;

public class ProxyValue extends RsonValue {

    private BaseValue value;

    private RsonValue configValue;

    private RuntimeContext context;

    public ProxyValue(BaseValue value, RsonValue configValue) {
        super(null);
        this.value = value;
        this.configValue = configValue;
    }

    @Override
    public BaseValue getField(String name) {
        BaseValue getCallback = configValue.getField("get");
        if (getCallback.getType() == ValueType.CLOSURE) {
            CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
            return executor.call(value, ((ClosureValue) getCallback), null, context, value, new StringValue(name));
        }
        if (value.getType() == ValueType.RSON) {
            return ((RsonValue) value).getField(name);
        } else {
            
            return value.getField(name);
        }
    }

    @Override
    public Map<String, BaseValue> getFields() {
        return value.getFields();
    }

    @Override
    public void putField(String name, BaseValue baseValue) {
        BaseValue putCallback = configValue.getField("put");
        if (putCallback instanceof ClosureValue) {
            CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
            executor.call(value, ((ClosureValue) putCallback), null, context, value, new StringValue(name), baseValue);
            return;
        }
        value.putField(name, baseValue);
    }

    public RsonValue getValue() {
        return value;
    }

}
