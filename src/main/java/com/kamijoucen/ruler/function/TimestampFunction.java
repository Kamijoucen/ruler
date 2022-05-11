package com.kamijoucen.ruler.function;

public class TimestampFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Timestamp";
    }

    @Override
    public Object call(Object... param) {
        return System.currentTimeMillis();
    }
}
