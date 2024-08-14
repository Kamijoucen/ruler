package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.DefaultScope;

import java.util.Map;

public class ModuleValue extends RsonValue {

    private final DefaultScope moduleScope;

    public ModuleValue(DefaultScope moduleScope) {
        this.moduleScope = moduleScope;
    }

    @Override
    public Map<String, BaseValue> getFields() {
        throw new UnsupportedOperationException("module value can not get fields");
    }

    public DefaultScope getModuleScope() {
        return moduleScope;
    }

}
