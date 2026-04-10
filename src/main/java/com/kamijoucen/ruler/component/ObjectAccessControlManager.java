package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;

public interface ObjectAccessControlManager {
    
    BaseValue accessObject(BaseValue value, String name, RuntimeContext context);

    void modifyObject(BaseValue value, String name, BaseValue newValue, RuntimeContext context);
    
}
