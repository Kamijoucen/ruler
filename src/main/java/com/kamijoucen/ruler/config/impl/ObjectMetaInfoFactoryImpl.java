package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.MetaInfoFactory;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.IRClassValue;
import com.kamijoucen.ruler.value.RClassValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class ObjectMetaInfoFactoryImpl implements MetaInfoFactory {

    public final Map<ValueType, IRClassValue> RCLASS_MAP = new HashMap<ValueType, IRClassValue>();

    public ObjectMetaInfoFactoryImpl() {
        RCLASS_MAP.put(ValueType.STRING, createStringClassValue());
        RCLASS_MAP.put(ValueType.ARRAY, createStringClassValue());
    }

    protected IRClassValue createStringClassValue() {
        return null;
    }

    protected IRClassValue createArrayClassValue() {
        IRClassValue val = new RClassValue(null);
        return val;
    }

    @Override
    public IRClassValue getClassValue(ValueType valueType) {
        IRClassValue value = RCLASS_MAP.get(valueType);
        AssertUtil.notNull(value);
        return value;
    }
}
