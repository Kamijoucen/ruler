package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public interface ValueConvert {

    ValueType getType();

    BaseValue realToBase(Object value, RulerConfiguration configuration);

    Object baseToReal(BaseValue value, RulerConfiguration configuration);

}
