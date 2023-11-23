package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class MakeItPossibleFunction implements RulerFunction {

    @Override
    public String getName() {
        return "makeItPossible";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        return "世之奇伟、瑰怪，非常之观，常在于险远，而人之所罕至焉，故非有志者不能至也。";
    }
}
