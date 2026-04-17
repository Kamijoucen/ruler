package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.parameter.RulerParameter;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, BaseValue> convertParamToBase(List<RulerParameter> params, RulerConfiguration configuration) {
        if (CollectionUtil.isEmpty(params)) {
            return Collections.emptyMap();
        }
        Map<String, BaseValue> values = new HashMap<>();
        for (RulerParameter param : params) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(param.getType());
            if (convert == null) {
                continue;
            }
            BaseValue baseValue = convert.realToBase(param.getValue(), configuration);
            values.put(param.getName(), baseValue);
        }
        return values;
    }

}
