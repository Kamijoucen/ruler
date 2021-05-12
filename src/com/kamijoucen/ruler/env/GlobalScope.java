package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalScope implements Scope {

    private Map<String, BaseValue> GLOBAL_VALUES = new ConcurrentHashMap<String, BaseValue>();

    @Override
    public BaseValue find(NameAST name) {
        BaseValue value = GLOBAL_VALUES.get(name.name.name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    @Override
    public void put(NameAST name, BaseValue baseValue) {
        GLOBAL_VALUES.put(name.name.name, baseValue);
    }

    @Override
    public Map<String, BaseValue> getReturnSpace() {
        return null;
    }

    @Override
    public void setReturnSpace() {
    }

    @Override
    public RulerFunction findFunction(String name) {
        return null;
    }

    @Override
    public void putReturnValue(String name, BaseValue value) {
    }

    @Override
    public boolean isContains(String name) {
        return false;
    }
}
