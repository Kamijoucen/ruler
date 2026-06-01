package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.StackDepthCheckOperation;
import com.kamijoucen.ruler.domain.runtime.TypeScope;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.Map;

public final class RuntimeContextFactory {

    private RuntimeContextFactory() {
    }

    public static RuntimeContext create(RulerConfiguration configuration, Map<String, BaseValue> outSpace) {
        RuntimeContext runtimeContext = new RuntimeContext(
                configuration.getEvalVisitor(),
                configuration.getTypeCheckVisitor(),
                configuration.getImportCache(),
                new StackDepthCheckOperation(),
                configuration);
        if (!CollectionUtil.isEmpty(outSpace)) {
            runtimeContext.setOutSpace(outSpace);
        }
        runtimeContext.setTypeScope(new TypeScope(null));
        return runtimeContext;
    }
}
