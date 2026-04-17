package com.kamijoucen.ruler.logic.function;

import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class FunctionParamUtil {

    public static StringValue string(BaseValue self, Object... param) {
        if (self instanceof StringValue) {
            return (StringValue) self;
        }
        if (param != null && param.length > 0 && param[0] instanceof StringValue) {
            return (StringValue) param[0];
        }
        return null;
    }

    public static ArrayValue array(BaseValue self, Object... param) {
        if (self instanceof ArrayValue) {
            return (ArrayValue) self;
        }
        if (param != null && param.length > 0 && param[0] instanceof ArrayValue) {
            return (ArrayValue) param[0];
        }
        return null;
    }

    public static int offset(BaseValue self) {
        if (self instanceof StringValue || self instanceof ArrayValue) {
            return 0;
        }
        return 1;
    }
}
