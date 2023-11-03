package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.ObjectAccessControlManager;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class ObjectAccessControlManagerImpl implements ObjectAccessControlManager {

    @Override
    public BaseValue accessObject(BaseValue value, String name, RuntimeContext context) {
        return null;
    }

    @Override
    public void modifyObject(BaseValue value, String name, BaseValue newValue, RuntimeContext context) {

    }

}
