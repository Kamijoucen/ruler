package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public interface Scope {

    //------------------------------------------------------
    boolean isContains(String name, boolean isOut);

    BaseValue findValue(String name, boolean isOut);

    void putValue(String name, boolean isOut, BaseValue baseValue);

    //------------------------------------------------------
    void initReturnSpace();

    void putReturnValue(BaseValue value);

    List<BaseValue> getReturnSpace();

    //------------------------------------------------------
    RulerFunction findFunction(String name, boolean isOut);

    void putFunction(RulerFunction function, boolean isOut);

}
