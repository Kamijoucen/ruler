package com.kamijoucen.ruler.types.spi;

import com.kamijoucen.ruler.logic.convert.HostCalls;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import java.util.Objects;

/** A host function binding. It never captures an engine or execution context. */
public final class HostFunction implements RulerFunction {
    private final RulerFunction function;

    public HostFunction(RulerFunction function) {
        this.function = Objects.requireNonNull(function, "function");
    }
    @Override public String getName() { return function.getName(); }
    @Override public BaseValue call(RuntimeContext context, Scope scope, BaseValue self, Object... args) {
        return HostCalls.call(function, context, scope, self, args);
    }
}
