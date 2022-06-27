package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public interface CreateRuntimeContextFactory {

    RuntimeContext create(Map<String, BaseValue> outSpace);

}