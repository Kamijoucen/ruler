package com.kamijoucen.ruler.logic.function.array;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;
import com.kamijoucen.ruler.logic.function.FunctionParamUtil;
import com.kamijoucen.ruler.logic.function.RulerFunction;

public class FindIndexFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayFindIndex";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayFindIndex expects an array");
        }
        if (param == null || param.length < off + 1) {
            return context.getConfiguration().getIntegerNumberCache().getValue(-1);
        }
        BaseValue callback = (BaseValue) param[off];
        int index = 0;
        for (BaseValue item : arr.getValues()) {
            BaseValue keep = callCallback(callback, currentScope, context, item,
                    context.getConfiguration().getIntegerNumberCache().getValue(index), arr);
            if (isTruthy(keep)) {
                return context.getConfiguration().getIntegerNumberCache().getValue(index);
            }
            index++;
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(-1);
    }

    private boolean isTruthy(BaseValue value) {
        if (value.getType() == ValueType.BOOL) {
            return ((BoolValue) value).getValue();
        }
        if (value.getType() == ValueType.NULL) {
            return false;
        }
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue() != 0;
        }
        if (value.getType() == ValueType.DOUBLE) {
            return ((DoubleValue) value).getValue() != 0;
        }
        return true;
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
        throw new RulerRuntimeException("arrayFindIndex expects a function");
    }
}
