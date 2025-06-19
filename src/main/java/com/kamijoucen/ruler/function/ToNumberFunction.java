package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * 转换为数字的函数
 *
 * @author Kamijoucen
 */
public class ToNumberFunction implements RulerFunction {

    @Override
    public String getName() {
        return "ToNumber";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return new IntegerValue(0);
        }

        BaseValue baseValue = (BaseValue) param[0];

        switch (baseValue.getType()) {
            case INTEGER:
                return baseValue;

            case DOUBLE:
                return baseValue;

            case STRING:
                String str = ((StringValue) baseValue).getValue();
                try {
                    // 尝试解析为整数
                    if (!str.contains(".")) {
                        return context.getConfiguration().getIntegerNumberCache()
                                .getValue(Long.parseLong(str));
                    }
                    // 解析为浮点数
                    return new DoubleValue(Double.parseDouble(str));
                } catch (NumberFormatException e) {
                    throw TypeException.cannotConvert(ValueType.STRING, ValueType.DOUBLE, null);
                }

            case BOOL:
                boolean boolValue = ((com.kamijoucen.ruler.value.BoolValue) baseValue).getValue();
                return context.getConfiguration().getIntegerNumberCache()
                        .getValue(boolValue ? 1 : 0);

            default:
                throw TypeException.cannotConvert(baseValue.getType(), ValueType.DOUBLE, null);
        }
    }
}
