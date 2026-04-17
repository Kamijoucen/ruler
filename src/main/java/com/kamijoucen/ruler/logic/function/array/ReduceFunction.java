package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class ReduceFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayReduce";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayReduce expects an array");
        }
        if (param == null || param.length < off + 1) {
            return null;
        }
        BaseValue callback = (BaseValue) param[off];
        boolean hasInitial = param.length >= off + 2;
        BaseValue accumulator = hasInitial ? (BaseValue) param[off + 1] : null;
        int startIndex = 0;

        if (!hasInitial) {
            if (arr.getValues().isEmpty()) {
                throw new RulerRuntimeException("arrayReduce of empty array with no initial value");
            }
            accumulator = arr.getValues().get(0);
            startIndex = 1;
        }

        for (int i = startIndex; i < arr.getValues().size(); i++) {
            BaseValue item = arr.getValues().get(i);
            accumulator = callCallback(callback, currentScope, context, accumulator, item,
                    context.getConfiguration().getIntegerNumberCache().getValue(i), arr);
        }
        return accumulator;
    }

    private BaseValue callCallback(BaseValue callback, Scope scope, RuntimeContext context,
                                   BaseValue acc, BaseValue item, BaseValue index, BaseValue arr) {
        if (callback.getType() == ValueType.CLOSURE) {
            return context.getConfiguration().getCallClosureExecutor()
                    .call((ClosureValue) callback, scope, context, acc, item, index, arr);
        } else if (callback.getType() == ValueType.FUNCTION) {
            return (BaseValue) ((FunctionValue) callback).getValue()
                    .call(context, scope, null, acc, item, index, arr);
        }
        throw new RulerRuntimeException("arrayReduce expects a function");
    }
}
