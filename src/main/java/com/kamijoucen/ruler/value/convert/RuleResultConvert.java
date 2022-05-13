package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.SubRuleValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.List;

public class RuleResultConvert implements ValueConvert {

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

        List<BaseValue> values = subRuleValue.getValues();
        for (BaseValue val : values) {
            // TODO
        }
        return null;
    }
}
