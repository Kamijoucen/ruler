package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.ArgumentException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DateValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Date;

/**
 * 日期时间函数
 * 用于创建和解析日期时间
 *
 * @author Kamijoucen
 */
public class DatetimeFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Datetime";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            // 无参数返回当前时间
            return new DateValue(new Date());
        }

        BaseValue baseValue = (BaseValue) param[0];

        if (baseValue.getType() != ValueType.STRING) {
            throw new ArgumentException("datetime函数只接受字符串类型参数", null);
        }

        String dateStr = ((StringValue) baseValue).getValue();
        // TODO: 实现日期解析
        return new DateValue(new Date());
    }
}
