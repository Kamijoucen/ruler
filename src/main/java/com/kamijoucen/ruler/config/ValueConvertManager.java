package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.ValueConvert;

public interface ValueConvertManager {

    ValueConvert getConverter(Object obj);

    ValueConvert getConverter(ValueType type);

}
