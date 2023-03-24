package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.Constant;
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

    private IRClassValue createStringClassValue() {
        return null;
    }

    private IRClassValue createArrayClassValue() {
        IRClassValue val = new RClassValue(null);
        val.getProperties().put(Constant.RCLASS_PROPERTIES, )
        return val;
    }

    @Override
    public IRClassValue getClassValue(ValueType valueType) {
        IRClassValue value = RCLASS_MAP.get(valueType);
        AssertUtil.notNull(value);
        return value;
    }
}
