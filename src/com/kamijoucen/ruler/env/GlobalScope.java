package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalScope extends Scope {

    private Map<String, BaseValue> GLOBAL_VALUES = new ConcurrentHashMap<String, BaseValue>();

    public GlobalScope() {
        super(null);
    }

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
}
