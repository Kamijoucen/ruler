package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.Scope;

import java.util.Map;

public class ModuleValue extends RsonValue {

    private final Scope moduleScope;

    public ModuleValue(Scope moduleScope) {
        this.moduleScope = moduleScope;
    }
    
    @Override
    public Map<String, BaseValue> getFields() {
        throw new UnsupportedOperationException("module value can not get fields");
    }

    public Scope getModuleScope() {
        return moduleScope;
    }

}
