package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.convert.ValueConvert;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtil {

    public static Number parseToNumber(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static BaseValue stringToValue(String str, RuntimeContext context) {
        Number result = parseToNumber(str);
        if (result == null) {
            return null;
        }
        if (result instanceof Double) {
            return new DoubleValue(result.doubleValue());
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(result.intValue());
    }

    public static BaseValue stringToValue(StringValue strValue, RuntimeContext context) {
        Number result = parseToNumber(strValue.getValue());
        if (result == null) {
            return strValue;
        }
        if (result instanceof Double) {
            return new DoubleValue(result.doubleValue());
        }
        return context.getConfiguration().getIntegerNumberCache().getValue(result.intValue());
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
