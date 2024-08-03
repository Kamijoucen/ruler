package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private final String stackName;
    private final Map<String, BaseValue> valueSpace;
    private final TokenLocation callLocation;

    public Scope(String stackName, TokenLocation callLocation) {
        this.stackName = stackName;
        this.valueSpace = new HashMap<>();
        this.callLocation = callLocation;
    }

    public String getStackName() {
        return stackName;
    }

    public BaseValue find(String name) {
        return valueSpace.get(name);
    }

    public void update(String name, BaseValue value) {
        valueSpace.put(name, value);
    }

    public void define(String name, BaseValue value) {
        if (valueSpace.get(name) != null) {
            // TODO
            throw SyntaxException.withSyntax("变量已定义：" + name);
        }
        valueSpace.put(name, value);
    }

    public void remove(String name) {
        valueSpace.remove(name);
    }

    public TokenLocation getCallLocation() {
        return callLocation;
    }
}
