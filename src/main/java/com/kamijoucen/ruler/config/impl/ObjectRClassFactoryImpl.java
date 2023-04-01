package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.config.RClassFactory;
import com.kamijoucen.ruler.function.ToStringFunction;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.RClass;
import com.kamijoucen.ruler.value.RClassValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class ObjectRClassFactoryImpl implements RClassFactory {

    public final Map<ValueType, RClass> RCLASS_MAP = new HashMap<ValueType, RClass>();

    public ObjectRClassFactoryImpl() {
        RCLASS_MAP.put(ValueType.STRING, createStringClassValue());
        RCLASS_MAP.put(ValueType.ARRAY, createArrayClassValue());
    }

    private RClass createBaseRClass() {

        RClass rClassValue = new RClassValue(null);

        ToStringFunction toStringVal = new ToStringFunction();
        // todo function class
        rClassValue.getProperties().put(toStringVal.getName(), new FunctionValue(toStringVal, null));

        return rClassValue;
    }

    private RClass createStringClassValue() {
        RClass baseRClass = createBaseRClass();
        return baseRClass;
    }

    private RClass createArrayClassValue() {
        RClass baseRClass = createBaseRClass();
        return baseRClass;
    }

    @Override
    public RClass getClassValue(ValueType valueType) {
        RClass value = RCLASS_MAP.get(valueType);
        AssertUtil.notNull(value);
        return value;
    }
}
