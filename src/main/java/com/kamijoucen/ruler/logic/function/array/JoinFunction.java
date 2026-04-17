package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class JoinFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayJoin";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayJoin expects an array");
        }
        if (param == null || param.length < off + 1 || !(param[off] instanceof StringValue)) {
            throw new RulerRuntimeException("arrayJoin expects an array and a separator string");
        }
        String sep = ((StringValue) param[off]).getValue();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.getValues().size(); i++) {
            if (i > 0) {
                sb.append(sep);
            }
            sb.append(arr.getValues().get(i).toString());
        }
        return new StringValue(sb.toString());
    }
}
