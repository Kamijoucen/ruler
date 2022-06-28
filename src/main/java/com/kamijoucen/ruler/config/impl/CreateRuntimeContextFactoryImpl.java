package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.CreateRuntimeContextFactory;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

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
        runtimeContext.setOutSpace(outSpace);
        return runtimeContext;
    }
}
