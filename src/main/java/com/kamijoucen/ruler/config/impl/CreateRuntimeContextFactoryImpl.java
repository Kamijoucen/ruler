package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.CreateRuntimeContextFactory;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public class CreateRuntimeContextFactoryImpl implements CreateRuntimeContextFactory {

    private final RulerConfiguration configuration;

    public CreateRuntimeContextFactoryImpl(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public RuntimeContext create(Map<String, BaseValue> outSpace) {
        if (CollectionUtil.isEmpty(outSpace)) {
            throw new IllegalArgumentException("out space is null");
        }
        RuntimeContext runtimeContext = new RuntimeContext(
                outSpace, new EvalVisitor(), new TypeCheckVisitor(), configuration.getImportCache(), configuration,
                configuration.getRuntimeBehaviorFactory().createStackDepthCheckOperation());
        return runtimeContext;
    }
}
