package com.kamijoucen.ruler.stdlib.classinfo;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.math.BigInteger;

public class LengthFunction implements RulerFunction {

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self,
            Object... param) {
        if (self.getType() == ValueType.ARRAY) {
            return IntegerValue.valueOf(BigInteger.valueOf(((ArrayValue) self).getValues().size()));
        } else if (self.getType() == ValueType.STRING) {
            StringValue stringValue = (StringValue) self;
            return IntegerValue.valueOf(BigInteger.valueOf(stringValue.getValue().length()));
        }
        return null;
    }

}
