package com.kamijoucen.ruler.value;

import java.util.Map;
import java.util.Objects;

import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;

public class ProxyValue extends RsonValue {

    private RsonValue value;

    private RsonValue configValue;

    private RuntimeContext context;

    public ProxyValue(RsonValue value, RsonValue rsonValue) {
        super(null);
        this.value = value;
        this.configValue = rsonValue;
    }

    @Override
    public BaseValue getField(String name) {
        BaseValue getCallback = configValue.getField("get");
        if (!(getCallback instanceof ClosureValue)) {
            return value.getField(name);
        }
        if (Objects.nonNull(getCallback)) {
            CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
            // TODO 这里的self指代原始value还是info value？
            return executor.call(value, ((ClosureValue) getCallback), null, context, value, new StringValue(name));
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
        if (!(putCallback instanceof ClosureValue)) {
            value.putField(name, baseValue);
            return;
        }
        if (Objects.nonNull(putCallback)) {
            CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
            // TODO 这里的self指代原始value还是info value？
            executor.call(value, ((ClosureValue) putCallback), null, context, value, new StringValue(name), baseValue);
        } else {
            value.putField(name, baseValue);
        }
    }

    public RsonValue getValue() {
        return value;
    }

}
