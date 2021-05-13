package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.exception.IllegalOperationException;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalScope implements Scope {

    private Map<String, BaseValue> globalValues;

    private Map<String, RulerFunction> functionSpace;

    public GlobalScope() {
        this.globalValues = new ConcurrentHashMap<String, BaseValue>();
        this.functionSpace = new ConcurrentHashMap<String, RulerFunction>();
    }

    @Override
    public boolean isContains(NameAST name) {
        return false;
    }

    @Override
    public BaseValue findValue(NameAST name) {
        BaseValue value = globalValues.get(name.name.name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    @Override
    public void putValue(NameAST name, BaseValue baseValue) {
        globalValues.put(name.name.name, baseValue);
    }

    @Override
    public Map<String, BaseValue> getReturnSpace() {
        throw new IllegalOperationException("非法的操作");
    }

    @Override
    public void initReturnSpace() {
        throw new IllegalOperationException("非法的操作");
    }

    @Override
    public RulerFunction findFunction(NameAST name) {
        return functionSpace.get(name.name.name);
    }

    @Override
    public void putFunction(RulerFunction function) {
        this.functionSpace.put(function.getName(), function);
    }

    @Override
    public void putReturnValue(String name, BaseValue value) {
        throw new IllegalOperationException("非法的操作");
    }

}
