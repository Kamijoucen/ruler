package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultScope implements Scope {

    private final Scope parent;

    private List<BaseValue> returnSpace;

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

    public boolean isContains(String name, boolean isOut) {
        if (getValueSpace(isOut).containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.isContains(name, isOut);
        }
        return false;
    }

    @Override
    public BaseValue findValue(String name, boolean isOut) {
        BaseValue baseValue = getValueSpace(isOut).get(name);
        if (baseValue == null && parent != null) {
            return parent.findValue(name, isOut);
        }
        return baseValue;
    }

    @Override
    public void putValue(String name, boolean isOut, BaseValue baseValue) {
        if (parent != null && parent.isContains(name, isOut)) {
            parent.putValue(name, isOut, baseValue);
        } else {
            getValueSpace(isOut).put(name, baseValue);
        }
    }

    @Override
    public void putReturnValue(BaseValue value) {
        if (this.returnSpace == null) {
            if (parent != null) {
                parent.putReturnValue(value);
            }
        } else {
            this.returnSpace.add(value);
        }
    }

    @Override
    public List<BaseValue> getReturnSpace() {
        return returnSpace;
    }

    @Override
    public void initReturnSpace() {
        this.returnSpace = new ArrayList<BaseValue>();
    }

    @Override
    public RulerFunction findFunction(String name, boolean isOut) {
        RulerFunction function = getFunctionSpace(isOut).get(name);
        if (function == null && parent != null) {
            return parent.findFunction(name, isOut);
        }
        return function;
    }

    @Override
    public void putFunction(RulerFunction function, boolean isOut) {
        this.getFunctionSpace(isOut).put(function.getName(), function);
    }

    private Map<String, BaseValue> getValueSpace(boolean isOut) {
        return isOut ? outValueSpace : valueSpace;
    }

    private Map<String, RulerFunction> getFunctionSpace(boolean isOut) {
        return isOut ? outFunctionSpace : functionSpace;
    }

}
