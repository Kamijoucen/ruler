package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface RulerFunction {

    String getName();

    Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param);

}
