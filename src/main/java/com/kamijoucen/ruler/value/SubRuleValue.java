package com.kamijoucen.ruler.value;

import java.util.List;

public class SubRuleValue extends AbstractValue {

    private String ruleName;
    private List<BaseValue> values;

    public SubRuleValue(String ruleName, List<BaseValue> values) {
        this.ruleName = ruleName;
        this.values = values;
    }

    @Override
    public ValueType getType() {
        return ValueType.RULE_RESULT;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public List<BaseValue> getValues() {
        return values;
    }

    public void setValues(List<BaseValue> values) {
        this.values = values;
    }
}
