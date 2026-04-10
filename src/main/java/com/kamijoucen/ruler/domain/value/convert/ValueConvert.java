package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public interface ValueConvert {

    ValueType getType();

    BaseValue realToBase(Object value, RulerConfiguration configuration);

    Object baseToReal(BaseValue value, RulerConfiguration configuration);

}
