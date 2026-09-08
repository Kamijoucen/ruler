package com.kamijoucen.ruler.stdlib.math;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DoubleValue;
import com.kamijoucen.ruler.types.spi.RulerFunction;

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
