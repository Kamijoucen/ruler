package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;

public interface RulerFunction {

    String getName();

    Object call(RuntimeContext context, Object... param);
}
