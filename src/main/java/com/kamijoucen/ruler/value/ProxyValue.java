package com.kamijoucen.ruler.value;

import java.util.Map;
import java.util.Objects;

import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.TokenType;

public class ProxyValue extends RsonValue {

    private RsonValue value;

    private ClosureValue getCallback;

    private ClosureValue putCallback;

    private RuntimeContext context;

    public ProxyValue(RsonValue value, ClosureValue getCallback, ClosureValue putCallback) {
        this.value = value;
        this.getCallback = getCallback;
        this.putCallback = putCallback;
    }

    @Override
    public BaseValue getField(String name) {
        BaseValue value = super.getField(name);
        if (Objects.nonNull(value)) {
            BinaryOperation callOperation = context.getConfiguration().getBinaryOperationFactory()
                    .findOperation(TokenType.CALL.name());
            Objects.requireNonNull(callOperation);
            // callOperation.invoke(null, null, null, context, null)
            // TODO callback
        }
        return value;
    }

    @Override
    public Map<String, BaseValue> getFields() {
        // TODO Auto-generated method stub
        return super.getFields();
    }

    @Override
    public void putField(String name, BaseValue baseValue) {
        // TODO Auto-generated method stub
        super.putField(name, baseValue);
    }

    public RsonValue getValue() {
        return value;
    }

    public ClosureValue getGetCallback() {
        return getCallback;
    }

    public ClosureValue getPutCallback() {
        return putCallback;
    }

}
