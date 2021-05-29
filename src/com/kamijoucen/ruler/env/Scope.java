package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public interface Scope {

    //------------------------------------------------------
    boolean isContains(String name, boolean isOut);

    BaseValue findValue(String name, boolean isOut);

    void putValue(String name, boolean isOut, BaseValue baseValue);

    //------------------------------------------------------
    void initReturnSpace();

    void putReturnValue(String name, BaseValue value);

    Map<String, BaseValue> getReturnSpace();

    //------------------------------------------------------
    RulerFunction findFunction(String name, boolean isOut);

    void putFunction(RulerFunction function, boolean isOut);

}
