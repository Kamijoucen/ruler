package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.value.IRClassValue;
import com.kamijoucen.ruler.value.ValueType;

public interface MetaInfoFactory {

    IRClassValue getClassValue(ValueType valueType);

}
