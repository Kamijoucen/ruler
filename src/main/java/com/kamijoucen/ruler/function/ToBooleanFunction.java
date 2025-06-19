package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

/**
 * 转换为布尔值的函数
 *
 * @author Kamijoucen
 */
public class ToBooleanFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ToBoolean";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return BoolValue.FALSE;
        }

        BaseValue baseValue = (BaseValue) param[0];

        switch (baseValue.getType()) {
            case BOOL:
                return baseValue;

            case INTEGER:
                long intValue = ((IntegerValue) baseValue).getValue();
                return intValue != 0 ? BoolValue.TRUE : BoolValue.FALSE;

            case DOUBLE:
                double doubleValue = ((DoubleValue) baseValue).getValue();
                return doubleValue != 0.0 ? BoolValue.TRUE : BoolValue.FALSE;

            case STRING:
                String strValue = ((StringValue) baseValue).getValue();
                // 空字符串为false，非空为true
                return strValue.isEmpty() ? BoolValue.FALSE : BoolValue.TRUE;

            case NULL:
                return BoolValue.FALSE;

            case ARRAY:
                // 空数组为false，非空为true
                ArrayValue arrayValue = (ArrayValue) baseValue;
                return arrayValue.getValues().isEmpty() ? BoolValue.FALSE : BoolValue.TRUE;

            default:
                throw TypeException.cannotConvert(baseValue.getType(), ValueType.BOOL, null);
        }
    }
}
