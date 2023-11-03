package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.RClassManager;
import com.kamijoucen.ruler.function.*;
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
        RCLASS_MAP.put(ValueType.STRING, createStringClass());
        RCLASS_MAP.put(ValueType.ARRAY, createArrayClass());
        RCLASS_MAP.put(ValueType.INTEGER, createIntegerClass());
    }

    private RClass createBaseRClass() {
        RClass rClass = new RClassValue();

        putFuncToRClass(new ToStringFunction(), rClass);
        putFuncToRClass(new PrintSelfFunction(), rClass);

        return rClass;
    }

    private RClass createStringClass() {
        RClass baseRClass = createBaseRClass();
        return baseRClass;
    }

    private RClass createArrayClass() {
        RClass rClass = createBaseRClass();
        putFuncToRClass(new LengthFunction(), rClass);
        return rClass;
    }

    private RClass createIntegerClass() {
        RClass rClass = createBaseRClass();
        return rClass;
    }

    @Override
    public RClass getClassValue(ValueType valueType) {
        RClass value = RCLASS_MAP.get(valueType);
        AssertUtil.notNull(value);
        return value;
    }


    private void putFuncToRClass(RulerFunction func, RClass rClass) {
        rClass.getProperties().put(func.getName(), new FunctionValue(func));
    }
}
