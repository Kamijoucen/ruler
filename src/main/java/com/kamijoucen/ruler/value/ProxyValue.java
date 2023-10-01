package com.kamijoucen.ruler.value;

import java.util.Map;
import java.util.Objects;

import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;

public class ProxyValue extends RsonValue {

    private RsonValue value;

    private ClosureValue getCallback;

    private ClosureValue putCallback;

    private RuntimeContext context;

    public ProxyValue(RsonValue value, ClosureValue getCallback, ClosureValue putCallback) {
        super(null);
        this.value = value;
        this.getCallback = getCallback;
        this.putCallback = putCallback;
    }

    @Override
    public BaseValue getField(String name) {

        if (Objects.nonNull(getCallback)) {
            CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
            // TODO 这里的self指代原始value还是info value？
            return executor.call(value, getCallback, null, context, value, new StringValue(name));
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

        if (Objects.nonNull(putCallback)) {
            CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
            // TODO 这里的self指代原始value还是info value？
            executor.call(value, putCallback, null, context, value, new StringValue(name), baseValue);
        } else {
            value.putField(name, baseValue);
        }
    }

    public RsonValue getValue() {
        return value;
    }

}
