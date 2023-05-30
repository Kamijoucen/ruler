package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.parameter.SubRuleResultValue;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.SubRuleValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubRuleResultConvert implements ValueConvert {

    @Override
    public ValueType getType() {
        return ValueType.RULE_RESULT;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        SubRuleValue subRuleValue = (SubRuleValue) value;
        if (CollectionUtil.isEmpty(subRuleValue.getValues())) {
            return new SubRuleResultValue(subRuleValue.getRuleName(), Collections.emptyList());
        }
        List<Object> realValue = new ArrayList<Object>(subRuleValue.getValues().size());
        for (BaseValue val : subRuleValue.getValues()) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(val.getType());
            realValue.add(convert.baseToReal(val, configuration));
        }
        return new SubRuleResultValue(subRuleValue.getRuleName(), realValue);
    }
}
