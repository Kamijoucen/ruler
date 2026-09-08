package com.kamijoucen.ruler.types.value;


public interface ValueConvert {

    ValueType getType();

    BaseValue realToBase(Object value);

    Object baseToReal(BaseValue value);

}
