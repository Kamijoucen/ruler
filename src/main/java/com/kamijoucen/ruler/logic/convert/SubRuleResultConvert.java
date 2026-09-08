package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.parameter.SubRuleResultValue;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.SubRuleValue;
import com.kamijoucen.ruler.types.value.ValueType;

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
            ValueConvert convert = ValueConversions.getConverter(val.getType());
            realValue.add(convert.baseToReal(val));
        }
        return new SubRuleResultValue(subRuleValue.getRuleName(), realValue);
    }
}
