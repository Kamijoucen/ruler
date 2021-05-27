package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.FunctionValue;

import java.util.Map;

public interface Scope {

    //------------------------------------------------------
    boolean isContains(NameAST name);

    BaseValue findValue(NameAST name);

    void putValue(NameAST name, BaseValue baseValue);

    //------------------------------------------------------
    void initReturnSpace();

    void putReturnValue(String name, BaseValue value);

    Map<String, BaseValue> getReturnSpace();

    //------------------------------------------------------
    RulerFunction findFunction(NameAST name);

    void putFunction(RulerFunction function, boolean isOut);

}
