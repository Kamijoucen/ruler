package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public interface Scope {

    BaseValue find(NameAST name);

    void put(NameAST name, BaseValue baseValue);

    Map<String, BaseValue> getReturnSpace();

    void setReturnSpace();

    RulerFunction findFunction(String name);

    void putReturnValue(String name, BaseValue value);

    boolean isContains(String name);

}
