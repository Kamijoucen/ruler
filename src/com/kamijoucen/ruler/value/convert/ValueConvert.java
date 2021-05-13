package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;

public interface ValueConvert {

    BaseValue realToBase(Object value);

    Object baseToReal(BaseValue value);

}
