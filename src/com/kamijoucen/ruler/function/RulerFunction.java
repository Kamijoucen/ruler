package com.kamijoucen.ruler.function;

public interface RulerFunction {

    String getName();

    Object call(Object... param);
}
