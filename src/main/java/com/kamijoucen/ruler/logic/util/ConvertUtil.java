package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.StringValue;

import java.math.BigDecimal;

public class ConvertUtil {

    public static Number parseToNumber(String str) {
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BaseValue stringToValue(String str, RuntimeContext context) {
        BigDecimal decimal;
        try {
            decimal = new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
        if (decimal.scale() <= 0 || decimal.stripTrailingZeros().scale() <= 0) {
            return context.getConfiguration().getIntegerNumberCache().getValue(decimal.toBigIntegerExact());
        }
        return new DoubleValue(decimal);
    }

    public static BaseValue stringToValue(StringValue strValue, RuntimeContext context) {
        return stringToValue(strValue.getValue(), context);
    }

}
