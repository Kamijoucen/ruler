package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

public interface ValueConvertManager {

    ValueConvert getConverter(Object obj);

    ValueConvert getConverter(ValueType type);

}
