package com.kamijoucen.ruler.runtime;

public interface RulerFunction {

    String getName();

    Object call(Object... param);
}
