package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.logic.util.NumberUtil;

public class AddOperation extends AbstractArithmeticOperation {

    @Override
    protected BaseValue computeInteger(IntegerValue l, IntegerValue r, RuntimeContext context) {
        return NumberUtil.add(l, r);
    }

    @Override
    protected BaseValue computeDecimal(DoubleValue l, DoubleValue r, RuntimeContext context) {
        return NumberUtil.add(l, r);
    }

    @Override
    protected String operationName() {
        return "'add'";
    }
}
