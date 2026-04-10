package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.Map;

public class CreateRuntimeContextFactoryImpl implements CreateRuntimeContextFactory {

    private final RulerConfiguration configuration;

    public CreateRuntimeContextFactoryImpl(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public RuntimeContext create(Map<String, BaseValue> outSpace) {
        RuntimeContext runtimeContext = new RuntimeContext(
                configuration.getEvalVisitor(),
                configuration.getTypeCheckVisitor(),
                configuration.getImportCache(),
                configuration.getRuntimeBehaviorFactory().createStackDepthCheckOperation(),
                configuration);
        if (!CollectionUtil.isEmpty(outSpace)) {
            runtimeContext.setOutSpace(outSpace);
        }
        return runtimeContext;
    }
}
