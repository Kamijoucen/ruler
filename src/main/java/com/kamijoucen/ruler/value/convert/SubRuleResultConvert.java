package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.common.ConvertRepository;
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
    public BaseValue realToBase(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object baseToReal(BaseValue value) {
        SubRuleValue subRuleValue = (SubRuleValue) value;
        if (CollectionUtil.isEmpty(subRuleValue.getValues())) {
            return new SubRuleResultValue(subRuleValue.getRuleName(), Collections.emptyList());
        }
        List<Object> realValue = new ArrayList<Object>(subRuleValue.getValues().size());
        for (BaseValue val : subRuleValue.getValues()) {
            realValue.add(ConvertRepository.getConverter(val.getType()).baseToReal(val));
        }
        return new SubRuleResultValue(subRuleValue.getRuleName(), realValue);
    }
}
