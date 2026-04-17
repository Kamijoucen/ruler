package com.kamijoucen.ruler.logic.function.classinfo;

import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.domain.value.ValueType;

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
            return context.getConfiguration().getIntegerNumberCache()
                    .getValue(BigInteger.valueOf(((ArrayValue) self).getValues().size()));
        } else if (self.getType() == ValueType.STRING) {
            StringValue stringValue = (StringValue) self;
            return context.getConfiguration().getIntegerNumberCache()
                    .getValue(BigInteger.valueOf(stringValue.getValue().length()));
        }
        return null;
    }

}
