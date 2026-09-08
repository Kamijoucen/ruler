package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

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
