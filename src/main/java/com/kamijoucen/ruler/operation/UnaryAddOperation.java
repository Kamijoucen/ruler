package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryAddOperation implements Operation {
    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {
        return param[0];
    }
}
