package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public interface RulerFunction2 {

    String getName();

    Object call(RuntimeContext context, BaseValue self, Object... param);

}
