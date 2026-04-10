package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;

public class AddOperation extends AbstractArithmeticOperation {

    @Override
    protected BaseValue computeLong(long l, long r, RuntimeContext context) {
        return context.getConfiguration().getIntegerNumberCache().getValue(l + r);
    }

    @Override
    protected BaseValue computeDouble(double l, double r, RuntimeContext context) {
        return new DoubleValue(l + r);
    }

    @Override
    protected String operationName() {
        return "'add'";
    }
}
