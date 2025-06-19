package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.ArgumentException;
import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * 字符串字符获取函数
 * 根据索引获取字符串中的字符
 *
 * @author Kamijoucen
 */
public class CharAtFunction implements RulerFunction {

    @Override
    public String getName() {
        return "StringCharAt";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            throw new ArgumentException("charAt函数需要2个参数", null);
        }

        BaseValue strValue = (BaseValue) param[0];
        BaseValue indexValue = (BaseValue) param[1];

        // 类型检查
        if (strValue.getType() != ValueType.STRING) {
            throw new ArgumentException("charAt函数第一个参数必须是字符串类型", null);
        }

        if (indexValue.getType() != ValueType.INTEGER) {
            throw new ArgumentException("charAt函数第二个参数必须是整数类型", null);
        }

                String str = ((StringValue) strValue).getValue();
        int index = (int) ((IntegerValue) indexValue).getValue();

        // 边界检查
        if (index < 0 || index >= str.length()) {
            throw RulerRuntimeException.indexOutOfBounds(index, str.length(), null);
        }

        return new StringValue(String.valueOf(str.charAt(index)));
    }
}
