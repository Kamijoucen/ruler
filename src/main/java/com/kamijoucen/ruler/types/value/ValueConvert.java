package com.kamijoucen.ruler.types.value;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ValueType;

public interface ValueConvert {

    ValueType getType();

    BaseValue realToBase(Object value);

    Object baseToReal(BaseValue value);

}
