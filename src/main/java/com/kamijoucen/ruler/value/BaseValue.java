package com.kamijoucen.ruler.value;

import java.io.Serializable;

public interface BaseValue extends Serializable {

    ValueType getType();

    IRClassValue getRClass();

}
