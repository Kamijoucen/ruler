package com.kamijoucen.ruler.function.classinfo;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class PushFunction implements RulerFunction {

    @Override
    public String getName() {
        return "push";
    }

    @Override
    public Object call(RuntimeContext context, Environment env, BaseValue self, Object... param) {
        if (self.getType() != ValueType.ARRAY) {
            throw new IllegalArgumentException("push function can only be called by array");
        }
        if (param == null || param.length == 0) {
            return null;
        }
        ArrayValue arrayValue = ((ArrayValue) self);
        arrayValue.getValues().add((BaseValue) param[0]);
        return param[0];
    }

}
