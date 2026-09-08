package com.kamijoucen.ruler.types.spi;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

/** A fixed name binding for a builtin implementation. */
public final class NamedFunction implements RulerFunction {
    private final String name;
    private final RulerFunction function;

    public NamedFunction(String name, RulerFunction function) {
        this.name = name;
        this.function = function;
    }
    @Override public String getName() { return name; }
    @Override public Object call(RuntimeContext context, Scope scope, BaseValue self, Object... args) {
        return function.call(context, scope, self, args);
    }
}
