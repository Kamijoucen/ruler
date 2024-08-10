package com.kamijoucen.ruler.function.classinfo;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

public class LengthFunction implements RulerFunction {

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public Object call(RuntimeContext context, Environment evn, BaseValue self,
            Object... param) {
        if (self.getType() == ValueType.ARRAY) {
            return context.getConfiguration().getIntegerNumberCache()
                    .getValue(((ArrayValue) self).getValues().size());
        } else if (self.getType() == ValueType.STRING) {
            StringValue stringValue = (StringValue) self;
            return context.getConfiguration().getIntegerNumberCache()
                    .getValue(stringValue.getValue().length());
        }
        return null;
    }

}
