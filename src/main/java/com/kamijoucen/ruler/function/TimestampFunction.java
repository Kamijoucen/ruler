package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;

public class TimestampFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Timestamp";
    }

    @Override
    public Object call(RuntimeContext context, Object... param) {
        return System.currentTimeMillis();
    }
}
