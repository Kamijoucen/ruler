package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;

public class SubOperation extends AbstractArithmeticOperation {

    @Override
    protected BaseValue computeLong(long l, long r, RuntimeContext context) {
        return new IntegerValue(l - r);
    }

    @Override
    protected BaseValue computeDouble(double l, double r, RuntimeContext context) {
        return new DoubleValue(l - r);
    }

    @Override
    protected String operationName() {
        return "'sub'";
    }
}
