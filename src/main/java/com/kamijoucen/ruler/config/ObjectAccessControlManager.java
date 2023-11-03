package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public interface ObjectAccessControlManager {
    
    BaseValue accessObject(BaseValue value, String name, RuntimeContext context);

    void modifyObject(BaseValue value, String name, BaseValue newValue, RuntimeContext context);
    
}
