package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.convert.ValueConvert;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtil {

    public static void main(String[] args) {
        System.out.println(stringToValue(new StringValue("-1.125")));
        // TODO 这种情况会失败
        System.out.println(stringToValue(new StringValue("+1.59")));
        System.out.println(stringToValue(new StringValue("15")));
        System.out.println(stringToValue(new StringValue("12.9")));
    }


    public static Number parseToNumber(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static BaseValue stringToValue(String str) {
        Number result = parseToNumber(str);
        if (result == null) {
            return null;
        }
        if (result instanceof Double) {
            return new DoubleValue(result.doubleValue());
        }
        return new IntegerValue(result.intValue());
    }

    public static BaseValue stringToValue(StringValue strValue) {
        Number result = parseToNumber(strValue.getValue());
        if (result == null) {
            return strValue;
        }
        if (result instanceof Double) {
            return new DoubleValue(result.doubleValue());
        }
        return new IntegerValue(result.intValue());
    }

    public static Map<String, BaseValue> convertParamToBase(List<RulerParameter> params, RulerConfiguration configuration) {
        if (CollectionUtil.isEmpty(params)) {
            return Collections.emptyMap();
        }
        Map<String, BaseValue> values = new HashMap<String, BaseValue>();
        for (RulerParameter param : params) {
            ValueConvert convert = ConvertRepository.getConverter(param.getType());
            if (convert == null) {
                continue;
            }
            BaseValue baseValue = convert.realToBase(param.getValue(), configuration);
            values.put(param.getName(), baseValue);
        }
        return values;
    }

}
