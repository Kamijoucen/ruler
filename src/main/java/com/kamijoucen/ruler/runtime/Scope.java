package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.value.BaseValue;

public interface Scope {

    String getName();

    BaseValue find(String name);

    void update(String name, BaseValue value);

    void define(String name, BaseValue value);

    void remove(String name);

}
