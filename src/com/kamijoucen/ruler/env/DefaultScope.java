package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.runtime.RulerFunctionProxy;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultScope implements Scope {

    private final Scope parent;

    private Map<String, BaseValue> returnSpace;

    private final Map<String, BaseValue> valueSpace;

    private final Map<String, BaseValue> outValueSpace;

    private final Map<String, RulerFunction> functionSpace;

    private final Map<String, RulerFunction> outFunctionSpace;

    public DefaultScope(Scope parent) {
        this(parent, false, false);
    }

    public DefaultScope(Scope parent, boolean isValueShared, boolean isFunctionShared) {
        this.parent = parent;
        this.valueSpace = isValueShared ? new ConcurrentHashMap<String, BaseValue>() : new HashMap<String, BaseValue>();
        this.outValueSpace = isValueShared ? new ConcurrentHashMap<String, BaseValue>() : new HashMap<String, BaseValue>();
        this.functionSpace = isFunctionShared ? new ConcurrentHashMap<String, RulerFunction>() : new HashMap<String, RulerFunction>();
        this.outFunctionSpace = isFunctionShared ? new ConcurrentHashMap<String, RulerFunction>() : new HashMap<String, RulerFunction>();
    }

    public boolean isContains(NameAST name) {
        if (getValueSpace(name.isOut).containsKey(name.name.name)) {
            return true;
        } else if (parent != null) {
            return parent.isContains(name);
        }
        return false;
    }

    @Override
    public BaseValue findValue(NameAST name) {
        BaseValue baseValue = getValueSpace(name.isOut).get(name.name.name);
        if (baseValue == null && parent != null) {
            return parent.findValue(name);
        }
        return baseValue;
    }

    @Override
    public void putValue(NameAST name, BaseValue baseValue) {
        if (parent != null && parent.isContains(name)) {
            parent.putValue(name, baseValue);
        } else {
            getValueSpace(name.isOut).put(name.name.name, baseValue);
        }
    }

    @Override
    public void putReturnValue(String name, BaseValue value) {
        if (this.returnSpace == null) {
            if (parent != null) {
                parent.putReturnValue(name, value);
            }
        } else {
            this.returnSpace.put(name, value);
        }
    }

    @Override
    public Map<String, BaseValue> getReturnSpace() {
        return returnSpace;
    }

    @Override
    public void initReturnSpace() {
        this.returnSpace = new HashMap<String, BaseValue>();
    }

    @Override
    public RulerFunction findFunction(NameAST name) {
        RulerFunction function = getFunctionSpace(name.isOut).get(name.name.name);
        if (function == null && parent != null) {
            return parent.findFunction(name);
        }
        return function;
    }

    @Override
    public void putFunction(RulerFunction function, boolean isOut) {
        this.getFunctionSpace(isOut).put(function.getName(), new RulerFunctionProxy(function));
    }

    private Map<String, BaseValue> getValueSpace(boolean isOut) {
        return isOut ? outValueSpace : valueSpace;
    }

    private Map<String, RulerFunction> getFunctionSpace(boolean isOut) {
        return isOut ? outFunctionSpace : functionSpace;
    }

}