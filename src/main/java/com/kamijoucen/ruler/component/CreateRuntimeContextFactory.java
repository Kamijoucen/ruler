package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.Map;

public interface CreateRuntimeContextFactory {

    RuntimeContext create(Map<String, BaseValue> outSpace);

}