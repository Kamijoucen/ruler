package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.value.RClass;
import com.kamijoucen.ruler.value.ValueType;

public interface RClassFactory {

    RClass getClassValue(ValueType valueType);

}
