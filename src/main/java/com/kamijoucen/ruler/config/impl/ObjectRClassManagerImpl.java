package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.RClassManager;
import com.kamijoucen.ruler.function.LengthFunction;
import com.kamijoucen.ruler.function.ToStringFunction;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.RClass;
import com.kamijoucen.ruler.value.RClassValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class ObjectRClassManagerImpl implements RClassManager {

    public final Map<ValueType, RClass> RCLASS_MAP = new HashMap<>();

    public ObjectRClassManagerImpl() {
        RCLASS_MAP.put(ValueType.STRING, createStringClassValue());
        RCLASS_MAP.put(ValueType.ARRAY, createArrayClassValue());
    }

    private RClass createBaseRClass() {

        RClass rClassValue = new RClassValue();

        ToStringFunction toStringVal = new ToStringFunction();
        // todo function class
        rClassValue.getProperties().put(toStringVal.getName(), new FunctionValue(toStringVal));

        return rClassValue;
    }

    private RClass createStringClassValue() {
        RClass baseRClass = createBaseRClass();
        return baseRClass;
    }

    private RClass createArrayClassValue() {
        RClass baseRClass = createBaseRClass();

        LengthFunction lengthFunction = new LengthFunction();
        baseRClass.getProperties().put(lengthFunction.getName(), new FunctionValue(lengthFunction));

        return baseRClass;
    }

    @Override
    public RClass getClassValue(ValueType valueType) {
        RClass value = RCLASS_MAP.get(valueType);
        AssertUtil.notNull(value);
        return value;
    }
}
