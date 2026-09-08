package com.kamijoucen.ruler.stdlib.array;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.*;
import com.kamijoucen.ruler.logic.eval.CallLogic;
import com.kamijoucen.ruler.stdlib.FunctionParamUtil;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FilterFunction implements RulerFunction {

    @Override
    public String getName() {
        return "arrayFilter";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        ArrayValue arr = FunctionParamUtil.array(self, param);
        int off = FunctionParamUtil.offset(self);
        if (arr == null) {
            throw new RulerRuntimeException("arrayFilter expects an array");
        }
        if (param == null || param.length < off + 1) {
            return null;
        }
        BaseValue callback = (BaseValue) param[off];
        List<BaseValue> result = new ArrayList<>();
        int index = 0;
        for (BaseValue item : arr.getValues()) {
            BaseValue keep = CallLogic.callValue(callback, currentScope, context, item,
                    IntegerValue.valueOf(BigInteger.valueOf(index)), arr);
            if (isTruthy(keep)) {
                result.add(item);
            }
            index++;
        }
        return new ArrayValue(result);
    }

    private boolean isTruthy(BaseValue value) {
        if (value.getType() == ValueType.BOOL) {
            return ((BoolValue) value).getValue();
        }
        if (value.getType() == ValueType.NULL) {
            return false;
        }
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue().compareTo(BigInteger.ZERO) != 0;
        }
        if (value.getType() == ValueType.DOUBLE) {
            return ((DoubleValue) value).getValue().compareTo(BigDecimal.ZERO) != 0;
        }
        return true;
    }

}
