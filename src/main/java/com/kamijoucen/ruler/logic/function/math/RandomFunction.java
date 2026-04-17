package com.kamijoucen.ruler.logic.function.math;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.math.BigDecimal;

public class RandomFunction implements RulerFunction {

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        return new DoubleValue(BigDecimal.valueOf(Math.random()));
    }
}
