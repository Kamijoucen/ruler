package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.ArrayList;
import java.util.List;

public class MapFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayMap";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayMap expects an array");
        }
        if (param == null || param.length < off + 1) {
            return null;
        }
        BaseValue callback = (BaseValue) param[off];
        List<BaseValue> result = new ArrayList<>();
        int index = 0;
        for (BaseValue item : arr.getValues()) {
            BaseValue mapped = callCallback(callback, currentScope, context, item,
                    context.getConfiguration().getIntegerNumberCache().getValue(index), arr);
            result.add(mapped);
            index++;
        }
        return new ArrayValue(result);
    }

    private BaseValue callCallback(BaseValue callback, Scope scope, RuntimeContext context,
                                   BaseValue item, BaseValue index, BaseValue arr) {
        if (callback.getType() == ValueType.CLOSURE) {
            return context.getConfiguration().getCallClosureExecutor()
                    .call((ClosureValue) callback, scope, context, item, index, arr);
        } else if (callback.getType() == ValueType.FUNCTION) {
            return (BaseValue) ((FunctionValue) callback).getValue()
                    .call(context, scope, null, item, index, arr);
        }
        throw new RulerRuntimeException("arrayMap expects a function");
    }
}
